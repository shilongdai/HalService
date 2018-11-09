package net.viperfish.halService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import net.viperfish.crawler.core.Datasink;
import net.viperfish.crawler.html.CrawledData;
import net.viperfish.crawler.html.HttpFetcher;
import net.viperfish.crawler.html.HttpWebCrawler;
import net.viperfish.crawler.html.RestrictionManager;
import net.viperfish.crawler.html.crawlHandler.MainPagePriorityBooster;
import net.viperfish.crawler.html.crawlHandler.TTLCrawlHandler;
import net.viperfish.crawler.html.restrictions.RobotsTxtRestrictionManager;
import net.viperfish.halService.core.DBCrawlChecker;
import net.viperfish.halService.core.HalIndexer;
import net.viperfish.halService.core.HeaderExtractionProcessor;
import net.viperfish.halService.core.IndexerDatasink;
import net.viperfish.halService.core.Limit2PatternHandler;
import net.viperfish.halService.core.MainRepository;
import net.viperfish.halService.core.ManagedHttpWebCrawler;
import net.viperfish.halService.core.ManagedServiceFetcher;
import net.viperfish.halService.core.RabbitMQHalIndexerProxy;
import net.viperfish.halService.core.TextExtractionTagProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@EnableAsync(proxyTargetClass = true, order = 1)
@EnableTransactionManagement(proxyTargetClass = true, order = Ordered.LOWEST_PRECEDENCE)
@EnableScheduling
@EnableJpaRepositories(basePackages = {
	"net.viperfish.halService"}, entityManagerFactoryRef = "entityManagerFactoryBean", transactionManagerRef = "jpaTransactionManager")
@ComponentScan(basePackages = "net.viperfish.halService", excludeFilters = @ComponentScan.Filter(Controller.class))
public class RootApplicationContextConfig implements AsyncConfigurer, SchedulingConfigurer {

	private Logger log = LogManager.getLogger();
	@Autowired
	private ServletContext context;
	@Autowired
	private MainRepository repo;

	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		String levelString = context.getInitParameter("threadNumber");
		if (levelString == null) {
			levelString = "4";
		}
		int level = Integer.parseInt(levelString);
		log.info("Creating thread pool with " + level + " threads");
		ThreadPoolTaskScheduler exec = new ThreadPoolTaskScheduler();
		exec.setPoolSize(level);
		exec.setThreadNamePrefix("transaction");
		exec.setAwaitTerminationSeconds(60);
		exec.setWaitForTasksToCompleteOnShutdown(true);
		exec.setRejectedExecutionHandler(new RejectedExecutionHandler() {

			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				StringBuilder errorBuilder = new StringBuilder("Task Rejected");
				log.error(errorBuilder.toString());
			}
		});
		return exec;
	}

	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean() throws ClassNotFoundException {
		LocalValidatorFactoryBean result = new LocalValidatorFactoryBean();
		result.setProviderClass(Class.forName("org.hibernate.validator.HibernateValidator"));
		result.setValidationMessageSource(this.messageSource());
		return result;
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor()
		throws ClassNotFoundException {
		MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
		processor.setValidator(this.localValidatorFactoryBean());
		return processor;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		TaskScheduler scheduler = this.taskScheduler();
		taskRegistrar.setTaskScheduler(scheduler);
	}

	@Bean
	public AsyncTaskExecutor processingExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(64);
		executor.setThreadNamePrefix("processor");
		executor.initialize();
		return executor;
	}

	@Bean
	public AsyncTaskExecutor fetchingExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(32);
		executor.setThreadNamePrefix("fetcher");
		executor.initialize();
		return executor;
	}

	@Override
	public Executor getAsyncExecutor() {
		Executor exec = this.taskScheduler();
		log.info(exec + " ready for use");
		return exec;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {

			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				StringBuilder errorBuilder = new StringBuilder("Async execution error on method:")
					.append(method.toString()).append(" with parameters:")
					.append(Arrays.toString(params));
				log.error(errorBuilder.toString());
			}
		};
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setCacheSeconds(-1);
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		messageSource
			.setBasenames("WEB-INF/i18n/messages", "WEB-INF/i18n/errors");
		return messageSource;
	}

	@Bean
	public DataSource mainRepoDatasource() {
		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		return lookup.getDataSource("jdbc/seMain");
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
		Map<String, Object> properties = new Hashtable<>();
		properties.put("javax.persistence.schema-generation.database.action", "none");
		properties.put("hibernate.connection.characterEncoding", "utf8");
		properties.put("hibernate.connection.useUnicode", "true");
		properties.put("hibernate.connection.charSet", "utf8");

		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(adapter);
		factory.setDataSource(this.mainRepoDatasource());
		factory.setPackagesToScan("net.viperfish.halService");
		factory.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
		factory.setValidationMode(ValidationMode.NONE);
		factory.setJpaPropertyMap(properties);
		return factory;
	}

	@Bean
	public PlatformTransactionManager jpaTransactionManager() {
		return new JpaTransactionManager(this.entityManagerFactoryBean().getObject());
	}

	@Bean
	public HalIndexer indexProxy() throws IOException {
		RabbitMQHalIndexerProxy proxy = new RabbitMQHalIndexerProxy(
			context.getInitParameter(ConfigMappings.QUEUE_HOST),
			context.getInitParameter(ConfigMappings.QUEUE_NAME));
		proxy.init();
		return proxy;
	}

	@Bean
	public Datasink<CrawledData> datasink() throws IOException {
		Datasink<CrawledData> result = new IndexerDatasink(this.indexProxy(), repo);
		result.init();
		return result;
	}

	@Bean
	public HttpFetcher fetcher() {
		HttpFetcher fetcher = new ManagedServiceFetcher("halbot", fetchingExecutor());
		RestrictionManager robotsTxt = new RobotsTxtRestrictionManager(
			context.getInitParameter(ConfigMappings.USER_AGENT));
		fetcher.registerRestrictionManager(robotsTxt);
		try {
			fetcher.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return fetcher;
	}

	@Bean
	public HttpWebCrawler crawler() throws IOException {
		ManagedHttpWebCrawler crawler = new ManagedHttpWebCrawler(processingExecutor(),
			datasink(),
			fetcher());
		Limit2PatternHandler patternHandler = new Limit2PatternHandler();
		DBCrawlChecker checker = new DBCrawlChecker(this.repo);
		patternHandler.addPattern(".*\\.edu");
		patternHandler.addPattern(Pattern
			.quote("https://searchenginesmarketer.com/company/resources/university-college-list/"));
		TTLCrawlHandler ttlChecker = new TTLCrawlHandler(3, 3);
		MainPagePriorityBooster booster = new MainPagePriorityBooster(10);
		crawler.registerCrawlerHandler(booster);
		crawler.registerCrawlerHandler(ttlChecker);
		crawler.registerCrawlerHandler(patternHandler);
		crawler.registerCrawlerHandler(checker);
		crawler.registerProcessor("texts", new TextExtractionTagProcessor());
		crawler.registerProcessor("headers", new HeaderExtractionProcessor());
		crawler.startProcessing();
		return crawler;
	}
}