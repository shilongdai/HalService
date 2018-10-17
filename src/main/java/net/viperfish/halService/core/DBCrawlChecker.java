package net.viperfish.halService.core;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.viperfish.crawler.html.CrawledData;
import net.viperfish.crawler.html.crawlHandler.BaseCrawlHandler;

public class DBCrawlChecker extends BaseCrawlHandler {

	private MainRepository mainRepo;
	private ConcurrentMap<URL, Boolean> tracker;
	private ConcurrentMap<String, Boolean> hashTracker;

	public DBCrawlChecker(MainRepository mainRepo) {
		super();
		this.mainRepo = mainRepo;
		tracker = new ConcurrentHashMap<>();
		hashTracker = new ConcurrentHashMap<>();
		loadEntries();
	}

	@Override
	protected boolean isParsed(CrawledData site) {
		return isFetched(site.getUrl()) && hashTracker.containsKey(site.getChecksum());
	}

	@Override
	protected boolean isFetched(URL url) {
		return tracker.containsKey(url);
	}

	@Override
	protected boolean lock(CrawledData s) {
		return tracker.putIfAbsent(s.getUrl(), true) == null
			&& hashTracker.putIfAbsent(s.getChecksum(), true) == null;
	}

	private void loadEntries() {
		for (SearchEngineCrawledData data : mainRepo.findAll()) {
			tracker.put(data.getUrl(), true);
			hashTracker.put(data.getChecksum(), true);
		}
	}

}
