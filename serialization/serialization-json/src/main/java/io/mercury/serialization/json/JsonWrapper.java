package io.mercury.serialization.json;

import javax.annotation.Nonnull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonWrapper {

	/**
	 * 普通JSON序列化
	 */
	private static final Gson Gson = new GsonBuilder().create();

	/**
	 * 以较高可视化的格式返回JSON
	 */
	private final static Gson GsonPrettyPrinting = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * JSON序列化, 包含Null值
	 */
	private static final Gson GsonHasNulls = new GsonBuilder().serializeNulls().create();

	/**
	 * 以漂亮的格式返回JSON, 包含Null值
	 */
	private static final Gson GsonPrettyPrintingHasNulls = new GsonBuilder().serializeNulls().setPrettyPrinting()
			.create();

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static final String toJson(@Nonnull Object obj) {
		return obj == null ? "null" : Gson.toJson(obj);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static final String toPrettyJson(@Nonnull Object obj) {
		return obj == null ? "null" : GsonPrettyPrinting.toJson(obj);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static final String toJsonHasNulls(@Nonnull Object obj) {
		return obj == null ? "null" : GsonHasNulls.toJson(obj);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static final String toPrettyJsonHasNulls(@Nonnull Object obj) {
		return obj == null ? "null" : GsonPrettyPrintingHasNulls.toJson(obj);
	}

}
