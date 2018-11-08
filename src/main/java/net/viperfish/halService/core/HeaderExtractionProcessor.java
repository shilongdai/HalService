package net.viperfish.halService.core;

import java.util.LinkedList;
import java.util.List;
import net.viperfish.crawler.html.CrawledData;
import net.viperfish.crawler.html.TagProcessor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HeaderExtractionProcessor implements TagProcessor {

	@Override
	public void processTag(Document document, CrawledData crawledData) {
		Elements hTags = document.select("h1, h2, h3, h4, h5, h6");
		List<Header> result = new LinkedList<>();

		for (Element e : hTags) {
			int size = Integer.parseInt(e.tagName().substring(1, 2));
			Header parsed = new Header(size, e.text());
			result.add(parsed);
		}
		crawledData.setProperty("headers", result);
	}
}
