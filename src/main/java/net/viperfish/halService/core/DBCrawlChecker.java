package net.viperfish.halService.core;

import java.net.URL;
import net.viperfish.crawler.html.CrawledData;
import net.viperfish.crawler.html.crawlHandler.BaseCrawlChecker;

public class DBCrawlChecker extends BaseCrawlChecker {

	private MapperRepository mapperRepository;

	public DBCrawlChecker(MapperRepository mapperRepository) {
		super();
		this.mapperRepository = mapperRepository;
	}

	@Override
	protected boolean isParsed(CrawledData site) {
		return isFetched(site.getUrl())
			&& mapperRepository.findByChecksum(site.getChecksum()) != null;
	}

	@Override
	protected synchronized boolean lock(CrawledData s) {
		if (isParsed(s)) {
			return false;
		}
		mapperRepository.save(new URLChecksumMapper(s.getUrl(), s.getChecksum()));
		return true;
	}

	@Override
	protected boolean isFetched(URL url) {
		return mapperRepository.findOne(url) != null;
	}
}
