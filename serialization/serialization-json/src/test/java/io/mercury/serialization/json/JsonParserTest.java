package io.mercury.serialization.json;

import org.junit.Test;

public class JsonParserTest {

	@Test
	public void test() {
		String json = "{\"A\":\"1\",\"B\":\"2\",\"C\":\"11\",\"D\":null,\"E\":null}";

		System.out.println(JsonParser2.isJsonObject(json));
		System.out.println(JsonParser2.toImmutableMap(json));
		
	}

}
