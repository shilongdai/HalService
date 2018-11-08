package net.viperfish.halService.core;

import java.net.MalformedURLException;
import java.net.URL;
import net.viperfish.crawler.core.Pair;
import net.viperfish.crawler.html.CrawledData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public final class TagProcessorTestTools {

	private TagProcessorTestTools() {

	}

	public static Pair<Document, CrawledData> toCrawledData(String html) {
		try {
			Document doc = Jsoup.parse(html);
			CrawledData crawled = new CrawledData();
			crawled.setTitle("Test Data");
			crawled.setUrl(new URL("https://test.com"));
			crawled.setChecksum("123");
			return new Pair<>(doc, crawled);
		} catch (MalformedURLException e) {
			throw new AssertionError("This cannot possibly happen");
		}
	}
}
