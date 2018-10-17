package net.viperfish.halService.core;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import net.viperfish.crawler.html.Anchor;
import net.viperfish.crawler.html.CrawledData;
import net.viperfish.halService.URL2StringConverter;
import net.viperfish.halService.URLSerializer;

@Entity
@Table(name = "Crawled")
public class SearchEngineCrawledData extends CrawledData {

	private long id;

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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Basic
	@Override
	@JsonGetter
	public String getTitle() {
		return super.getTitle();
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
	}

	@Basic
	@Convert(converter = URL2StringConverter.class)
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

	@Basic
	@Override
	@JsonGetter
	public String getContent() {
		return super.getContent();
	}

	@Override
	public void setContent(String content) {
		super.setContent(content);
	}

	@Basic
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
	@Transient
	@Override
	public List<Anchor> getAnchors() {
		return super.getAnchors();
	}

	@Override
	public void setAnchors(List<Anchor> anchors) {
		super.setAnchors(anchors);
	}

	@JsonAnyGetter
	@Transient
	@Override
	public Map<String, Object> getProperties() {
		return super.getProperties();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		SearchEngineCrawledData that = (SearchEngineCrawledData) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), id);
	}
}
