package net.viperfish.halService.core;

import java.util.concurrent.Future;
import net.viperfish.crawler.core.Datasink;
import net.viperfish.crawler.core.ProcessedResult;
import net.viperfish.crawler.html.CrawledData;
import net.viperfish.crawler.html.FetchedContent;
import net.viperfish.crawler.html.HttpFetcher;
import net.viperfish.crawler.html.HttpWebCrawler;
import net.viperfish.crawler.html.exception.ParsingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.task.AsyncTaskExecutor;

public class ManagedHttpWebCrawler extends HttpWebCrawler {

	private Logger logger;
	private AsyncTaskExecutor processorPool;

	public ManagedHttpWebCrawler(AsyncTaskExecutor threadpool, Datasink<CrawledData> db,
		HttpFetcher fetcher) {
		super(db, fetcher);
		this.processorPool = threadpool;
		logger = LogManager.getLogger();
	}

	@Override
	protected Future<?> runDelegator(Runnable delegatorTask) {
		return processorPool.submit(delegatorTask);
	}

	@Override
	protected Future<?> runProcessor(Runnable processor) {
		return processorPool.submit(processor);
	}

	@Override
	protected void cleanup() {
	}

	@Override
	protected ProcessedResult<CrawledData> process(FetchedContent content) throws ParsingException {
		logger.info("Processing:" + content.getUrl().getSource() + " Priority:" + content.getUrl()
			.getPriority());
		return super.process(content);
	}

	@Override
	protected void processFetchError(Throwable e) {
		logger.error("Error Fetching:", e);
	}

	@Override
	protected void handleProcessingError(Throwable e) {
		logger.error("Error Processing:", e);
	}
}
