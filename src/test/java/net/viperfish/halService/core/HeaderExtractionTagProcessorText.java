package net.viperfish.halService.core;

import java.util.List;
import net.viperfish.crawler.core.Pair;
import net.viperfish.crawler.html.CrawledData;
import net.viperfish.crawler.html.exception.ParsingException;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class HeaderExtractionTagProcessorText {

	@Test
	public void testExtraction() throws ParsingException {
		HeaderExtractionProcessor processor = new HeaderExtractionProcessor();
		String toTest = "<html><body><h1>This is header</h1><p>Test Paragraph</p><q>quote</q><p>test paragraph again</p><h2>This is another header</h2></body></html>";
		Pair<Document, CrawledData> crawled = TagProcessorTestTools.toCrawledData(toTest);
		processor.processTag(crawled.getFirst(), crawled.getSecond());

		Header expected1 = new Header(1, "This is header");
		Header expected2 = new Header(2, "This is another header");
		List<Header> headers = crawled.getSecond().getProperty("headers", List.class);
		Assert.assertEquals(2, headers.size());
		Assert.assertTrue(headers.contains(expected1));
		Assert.assertTrue(headers.contains(expected2));
	}

}
