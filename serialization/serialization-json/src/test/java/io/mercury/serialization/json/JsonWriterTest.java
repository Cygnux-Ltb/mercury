package io.mercury.serialization.json;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class JsonWrapperTest {

	@Test
	public void test() {

		Map<String, String> map = new HashMap<>();
		map.put("A", "1");
		map.put("B", "2");
		map.put("C", "11");
		map.put("D", null);
		map.put("E", null);

		System.out.println(JsonWrapper.toJson(null));
		System.out.println(JsonWrapper.toJsonHasNulls(null));
		System.out.println(JsonWrapper.toJson(map));
		System.out.println(JsonWrapper.toJsonHasNulls(map));

	}

}
