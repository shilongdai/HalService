package net.viperfish.halService.core;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.viperfish.crawler.html.Anchor;
import net.viperfish.crawler.html.CrawledData;
import net.viperfish.halService.URLSerializer;


public class SearchEngineCrawledData extends CrawledData {

	public SearchEngineCrawledData() {
		super();
	}

	public SearchEngineCrawledData(CrawledData copy) {
		this.setAnchors(copy.getAnchors());
		this.setUrl(copy.getUrl());
		this.setChecksum(copy.getChecksum());
		this.setContent(copy.getContent());
		this.setTitle(copy.getTitle());
		for (Entry<String, Object> e : copy.getProperties().entrySet()) {
			this.setProperty(e.getKey(), e.getValue());
		}
	}


	@Override
	@JsonGetter
	public String getTitle() {
		return super.getTitle();
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
	}

	@Override
	@JsonGetter
	@JsonSerialize(using = URLSerializer.class)
	public URL getUrl() {
		return super.getUrl();
	}

	@Override
	public void setUrl(URL url) {
		super.setUrl(url);
	}

	@Override
	@JsonGetter
	public String getContent() {
		return super.getContent();
	}

	@Override
	public void setContent(String content) {
		super.setContent(content);
	}

	@Override
	@JsonGetter
	public String getChecksum() {
		return super.getChecksum();
	}

	@Override
	public void setChecksum(String checksum) {
		super.setChecksum(checksum);
	}

	@JsonGetter
	@Override
	public List<Anchor> getAnchors() {
		return super.getAnchors();
	}

	@Override
	public void setAnchors(List<Anchor> anchors) {
		super.setAnchors(anchors);
	}

	@JsonAnyGetter
	@Override
	public Map<String, Object> getProperties() {
		return super.getProperties();
	}

}
