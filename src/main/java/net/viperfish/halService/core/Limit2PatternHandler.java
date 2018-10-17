package net.viperfish.halService.core;

import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import net.viperfish.crawler.html.HandlerResponse;
import net.viperfish.crawler.html.crawlHandler.YesCrawlChecker;

public class Limit2PatternHandler extends YesCrawlChecker {

	private Set<Pattern> patterns;

	public Limit2PatternHandler() {
		patterns = Collections.newSetFromMap(new ConcurrentHashMap<>());
	}

	public void addPattern(String pattern) {
		this.patterns.add(Pattern.compile(pattern));
	}

	@Override
	public HandlerResponse handlePreFetch(URL url) {
		if (patterns.isEmpty()) {
			return HandlerResponse.GO_AHEAD;
		}
		for (Pattern pattern : patterns) {
			if (pattern.matcher(url.toExternalForm()).find()) {
				return HandlerResponse.GO_AHEAD;
			}
		}
		return HandlerResponse.HALT;
	}
}
