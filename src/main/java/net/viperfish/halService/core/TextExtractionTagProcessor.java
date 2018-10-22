package net.viperfish.halService.core;

import java.util.LinkedList;
import java.util.List;
import net.viperfish.crawler.html.CrawledData;
import net.viperfish.crawler.html.TagProcessor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TextExtractionTagProcessor implements TagProcessor {

	@Override
	public void processTag(Document document, CrawledData crawledData) {
		Elements selectedParagraphs = document.select("p");
		List<String> result = new LinkedList<>();
		for (Element element : selectedParagraphs) {
			result.add(element.text());
		}
		crawledData.setProperty("text-sections", result);
	}
}
