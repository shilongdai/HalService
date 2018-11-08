package net.viperfish.halService.core;

import java.util.List;
import net.viperfish.crawler.core.Pair;
import net.viperfish.crawler.html.CrawledData;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class TextExtractionTagProcessorTest {

	@Test
	public void testParagraphExtraction() {
		TextExtractionTagProcessor processor = new TextExtractionTagProcessor();
		String toTest = "<html><body><p>Test Paragraph</p><q>quote</q><p>test paragraph again</p></body></html>";
		Pair<Document, CrawledData> crawled = TagProcessorTestTools.toCrawledData(toTest);

		processor.processTag(crawled.getFirst(), crawled.getSecond());
		List<String> texts = crawled.getSecond().getProperty("text-sections", List.class);

		Assert.assertNotEquals(null, texts);
		Assert.assertEquals(2, texts.size());
		Assert.assertTrue(texts.contains("Test Paragraph"));
		Assert.assertTrue(texts.contains("test paragraph again"));
	}

}
