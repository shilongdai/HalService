package net.viperfish.halService;

import java.net.MalformedURLException;
import java.net.URL;
import javax.persistence.AttributeConverter;

public class URL2StringConverter implements AttributeConverter<URL, String> {

	@Override
	public String convertToDatabaseColumn(URL attribute) {
		return attribute.toString();
	}

	@Override
	public URL convertToEntityAttribute(String dbData) {
		try {
			return new URL(dbData);
		} catch (MalformedURLException e) {
			return null;
		}
	}
}
