package net.viperfish.halService.core;

import java.util.Objects;

public class Header {

	private int size;
	private String text;

	public Header() {
		this(1, "");
	}

	public Header(int size, String text) {
		this.size = size;
		this.text = text;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Header header = (Header) o;
		return size == header.size &&
			Objects.equals(text, header.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(size, text);
	}
}
