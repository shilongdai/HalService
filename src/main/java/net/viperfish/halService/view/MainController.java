package net.viperfish.halService.view;

import java.net.MalformedURLException;
import java.net.URL;
import net.viperfish.crawler.html.HttpWebCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController {

	@Autowired
	private HttpWebCrawler crawler;

	@RequestMapping(name = "index", method = RequestMethod.GET)
	public String mainPage() {
		return "index";
	}

	@RequestMapping(name = "submit", method = RequestMethod.POST)
	public View submitURL(@RequestParam("url") String url) {
		try {
			crawler.submit(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new RedirectView("index");
	}

}
