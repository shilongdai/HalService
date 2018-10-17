package net.viperfish.halService;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.net.URL;

public class URLSerializer extends StdSerializer<URL> {

	public URLSerializer() {
		super(URL.class);
	}

	@Override
	public void serialize(URL value, JsonGenerator gen, SerializerProvider provider)
		throws IOException {
		gen.writeString(value.toString());
	}


}
