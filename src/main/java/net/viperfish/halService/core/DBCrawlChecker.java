package net.viperfish.halService.core;

import net.viperfish.crawler.html.crawlHandler.BaseInMemCrawlChecker;

public class DBCrawlChecker extends BaseInMemCrawlChecker {

	private MainRepository mainRepo;

	public DBCrawlChecker(MainRepository mainRepo) {
		super();
		this.mainRepo = mainRepo;
		loadEntries();
	}

	private void loadEntries() {
		for (SearchEngineCrawledData data : mainRepo.findAll()) {
			getURLTracker().put(data.getUrl(), true);
			getHashTracker().put(data.getChecksum(), true);
		}
	}

}
