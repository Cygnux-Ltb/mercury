package io.mercury.codec.json;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonEncoder {

	private static final Gson GsonNormal = new GsonBuilder().create();

	public static final String toJson(@Nonnull Object obj) {
		return GsonNormal.toJson(obj);
	}

	private static final Gson GsonHasNulls = new GsonBuilder().serializeNulls().create();

	public static final String toJsonHasNulls(@Nonnull Object obj) {
		return GsonHasNulls.toJson(obj);
	}

	public static void main(String[] args) {

		Map<String, String> map = new HashMap<>();
		map.put("A", "1");
		map.put("B", "2");
		map.put("C", "11");
		map.put("D", null);
		map.put("E", null);

		System.out.println(toJson(map));
		System.out.println(toJsonHasNulls(map));

		System.out.println(JSON.toJSONString(map));

	}

}
