package net.viperfish.halService.core;

import java.net.URL;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import net.viperfish.halService.URL2StringConverter;

@Entity
@Table(name = "CrawledPages")
public class URLChecksumMapper {

	private URL url;
	private String checksum;

	public URLChecksumMapper() {
		url = null;
		checksum = null;
	}

	public URLChecksumMapper(URL url, String checksum) {
		this.url = url;
		this.checksum = checksum;
	}

	@Id
	@Convert(converter = URL2StringConverter.class)
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@Basic
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		URLChecksumMapper that = (URLChecksumMapper) o;
		return Objects.equals(url, that.url) &&
			Objects.equals(checksum, that.checksum);
	}

	@Override
	public int hashCode() {
		return Objects.hash(url, checksum);
	}
}
