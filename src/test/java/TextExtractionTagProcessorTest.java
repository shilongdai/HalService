import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import net.viperfish.crawler.html.CrawledData;
import net.viperfish.crawler.html.exception.ParsingException;
import net.viperfish.halService.core.TextExtractionTagProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class TextExtractionTagProcessorTest {

	@Test
	public void testParagraphExtraction() throws MalformedURLException, ParsingException {
		TextExtractionTagProcessor processor = new TextExtractionTagProcessor();
		String toTest = "<html><body><p>Test Paragraph</p><q>quote</q><p>test paragraph again</p></body></html>";
		Document doc = Jsoup.parse(toTest);
		CrawledData crawled = new CrawledData();
		crawled.setTitle("Test Data");
		crawled.setUrl(new URL("https://test.com"));
		crawled.setChecksum("123");

		processor.processTag(doc, crawled);
		List<String> texts = crawled.getProperty("text-sections", List.class);

		Assert.assertNotEquals(null, texts);
		Assert.assertEquals(2, texts.size());
		Assert.assertTrue(texts.contains("Test Paragraph"));
		Assert.assertTrue(texts.contains("test paragraph again"));
	}

}
