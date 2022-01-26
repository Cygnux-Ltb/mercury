package io.mercury.serialization.json;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author yellow013
 *
 *         TODO 优化实现
 *
 */
public final class JsonWrapper {

	// 普通JSON序列化
	private static final Gson Gson = new GsonBuilder().create();

	// JSON序列化, 包含Null值
	private static final Gson GsonHasNulls = new GsonBuilder().serializeNulls().create();

	// 以较高可视化的格式返回JSON
	private final static Gson GsonPretty = new GsonBuilder().setPrettyPrinting().create();

	// 以漂亮的格式返回JSON, 包含Null值
	private static final Gson GsonPrettyHasNulls = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static final String toJson(@Nullable Object obj) {
		return toJson0(Gson, obj);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static final String toPrettyJson(@Nullable Object obj) {
		return toJson0(GsonPretty, obj);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static final String toJsonHasNulls(@Nullable Object obj) {
		return toJson0(GsonHasNulls, obj);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static final String toPrettyJsonHasNulls(@Nullable Object obj) {
		return toJson0(GsonPrettyHasNulls, obj);
	}

	private static final String toJson0(final Gson gson, final Object obj) {
		if (obj == null)
			return "null";
		return gson.toJson(obj);
	}

}
