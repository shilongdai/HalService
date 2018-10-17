package net.viperfish.halService.core;

import java.io.IOException;
import javax.annotation.PreDestroy;
import net.viperfish.crawler.core.Datasink;
import net.viperfish.crawler.html.CrawledData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IndexerDatasink implements Datasink<CrawledData> {

	private HalIndexer proxy;
	private MainRepository repo;
	private Logger logger;
	private boolean isClosed;

	public IndexerDatasink(HalIndexer indexer, MainRepository repo) {
		this.proxy = indexer;
		this.repo = repo;
		isClosed = false;
	}

	@Override
	public void init() {
		logger = LogManager.getLogger();
	}

	@Override
	public void write(CrawledData data) throws IOException {
		logger.info("Writing:" + data.getUrl());
		SearchEngineCrawledData convert = new SearchEngineCrawledData(data);
		repo.save(convert);
		proxy.index(convert);
		logger.debug("Saved ID:" + convert.getId());
	}

	@Override
	public boolean isClosed() {
		return isClosed;
	}

	@PreDestroy
	@Override
	public void close() {
		isClosed = true;
	}
}
