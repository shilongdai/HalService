package net.viperfish.halService;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.net.URL;

public class URLSerializer extends StdSerializer<URL> {

	public URLSerializer(Class<URL> t) {
		super(t);
	}

	public URLSerializer(JavaType type) {
		super(type);
	}

	public URLSerializer(Class<?> t, boolean dummy) {
		super(t, dummy);
	}

	public URLSerializer(StdSerializer<?> src) {
		super(src);
	}

	@Override
	public void serialize(URL value, JsonGenerator gen, SerializerProvider provider)
		throws IOException {
		gen.writeString(value.toString());
	}


}
