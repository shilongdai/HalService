package net.viperfish.halService.compression;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import javax.persistence.AttributeConverter;

public class HtmlCompressor implements AttributeConverter<String, byte[]> {

	@Override
	public String convertToEntityAttribute(byte[] dbData) {
		try {
			Compressor compressor = Compressors.getCompressor("GZ");
			byte[] deCompressed = compressor.deflate(dbData);
			return new String(deCompressed, StandardCharsets.UTF_8);
		} catch (NoSuchAlgorithmException e) {
			// will not happen
			throw new AssertionError("Impossible!");
		}
	}

	@Override
	public byte[] convertToDatabaseColumn(String attribute) {
		try {
			Compressor compressor = Compressors.getCompressor("GZ");
			byte[] compressed = compressor.compress(attribute.getBytes(StandardCharsets.UTF_8));
			return compressed;
		} catch (NoSuchAlgorithmException e) {
			// will not happen
			throw new AssertionError("Impossible!");
		}
	}
}
