package net.viperfish.halService.core;

import javax.annotation.PreDestroy;
import net.viperfish.crawler.core.Datasink;
import net.viperfish.crawler.core.ProcessedResult;
import net.viperfish.crawler.html.CrawledData;
import net.viperfish.crawler.html.FetchedContent;
import net.viperfish.crawler.html.HttpFetcher;
import net.viperfish.crawler.html.HttpWebCrawler;
import net.viperfish.crawler.html.exception.ParsingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ManagedHttpWebCrawler extends HttpWebCrawler {

	private Logger logger;

	public ManagedHttpWebCrawler(int threadCount,
		Datasink<CrawledData> db,
		HttpFetcher fetcher) {
		super(threadCount, db, fetcher);
		logger = LogManager.getLogger();
	}

	@Override
	protected ProcessedResult<CrawledData> process(FetchedContent content) throws ParsingException {
		logger.info("Processing:" + content.getUrl().getToFetch() + " Priority:" + content.getUrl()
			.getPriority());
		return super.process(content);
	}

	@PreDestroy
	@Override
	public void shutdown() {
		super.shutdown();
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
