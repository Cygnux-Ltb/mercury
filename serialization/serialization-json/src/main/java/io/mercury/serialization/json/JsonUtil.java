package io.mercury.serialization.json;

//import static com.alibaba.fastjson.JSON.parseArray;
//import static com.alibaba.fastjson.JSON.parseObject;
//import static com.alibaba.fastjson.JSONValidator.from;
//import static io.mercury.common.collections.ImmutableLists.newImmutableList;
//import static io.mercury.common.collections.ImmutableMaps.newImmutableMap;
//import static io.mercury.common.collections.MutableLists.newFastList;
//import static io.mercury.common.collections.MutableMaps.newUnifiedMap;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Nonnull;
//
//import org.eclipse.collections.api.list.ImmutableList;
//import org.eclipse.collections.api.list.MutableList;
//import org.eclipse.collections.api.map.ImmutableMap;
//import org.eclipse.collections.api.map.MutableMap;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.JSONValidator.Type;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import io.mercury.serialization.json.JsonParser.JsonParseException;
//
//@Deprecated
//public final class JsonUtil {
//
//	/**
//	 * 普通JSON序列化
//	 */
//	private static final Gson Gson = new GsonBuilder().create();
//
//	/**
//	 * 以漂亮的格式返回JSON
//	 */
//	private final static Gson GsonPrettyPrinting = new GsonBuilder().setPrettyPrinting().create();
//
//	/**
//	 * JSON序列化, 包含Null值
//	 */
//	private static final Gson GsonHasNulls = new GsonBuilder().serializeNulls().create();
//
//	/**
//	 * 以漂亮的格式返回JSON, 包含Null值
//	 */
//	private static final Gson GsonPrettyPrintingHasNulls = new GsonBuilder().serializeNulls().setPrettyPrinting()
//			.create();
//
//	/**
//	 * 
//	 * @param obj
//	 * @return
//	 */
//	public static final String toJson(@Nonnull Object obj) {
//		return obj == null ? "null" : Gson.toJson(obj);
//	}
//
//	/**
//	 * 
//	 * @param obj
//	 * @return
//	 */
//	public static final String toPrettyJson(@Nonnull Object obj) {
//		return obj == null ? "null" : GsonPrettyPrinting.toJson(obj);
//	}
//
//	/**
//	 * 
//	 * @param obj
//	 * @return
//	 */
//	public static final String toJsonHasNulls(@Nonnull Object obj) {
//		return obj == null ? "null" : GsonHasNulls.toJson(obj);
//	}
//
//	/**
//	 * 
//	 * @param obj
//	 * @return
//	 */
//	public static final String toPrettyJsonHasNulls(@Nonnull Object obj) {
//		return obj == null ? "null" : GsonPrettyPrintingHasNulls.toJson(obj);
//	}
//
//	/**
//	 * 
//	 * @param str
//	 * @return
//	 */
//	public static boolean isJsonValue(String str) {
//		return from(str).getType() == Type.Value;
//	}
//
//	/**
//	 * 
//	 * @param str
//	 * @return
//	 */
//	public static boolean isJsonArray(String str) {
//		return from(str).getType() == Type.Array;
//	}
//
//	/**
//	 * 
//	 * @param str
//	 * @return
//	 */
//	public static boolean isJsonObject(String str) {
//		return from(str).getType() == Type.Object;
//	}
//
//	/**
//	 * 
//	 */
//	public static final JSONObject toJsonObject(@Nonnull String json) throws JsonParseException {
//		try {
//			return parseObject(json);
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param json
//	 * @return
//	 * @throws JsonParseException
//	 */
//	public static final JSONArray toJsonArray(@Nonnull String json) throws JsonParseException {
//		try {
//			return parseArray(json);
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param <T>
//	 * @param json
//	 * @param clazz
//	 * @return
//	 * @throws JsonParseException
//	 */
//	public static final <T> T toObject(@Nonnull String json, @Nonnull Class<T> clazz) throws JsonParseException {
//		try {
//			return parseObject(json, clazz);
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param json
//	 * @return
//	 * @throws JsonParseException
//	 */
//	public static final List<Object> toList(@Nonnull String json) throws JsonParseException {
//		try {
//			return parseArray(json);
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param <T>
//	 * @param json
//	 * @param clazz
//	 * @return
//	 * @throws JsonParseException
//	 */
//	public static final <T> List<T> toList(@Nonnull String json, @Nonnull Class<T> clazz) throws JsonParseException {
//		try {
//			return parseArray(json, clazz);
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param json
//	 * @return
//	 * @throws JsonParseException
//	 */
//	public static final MutableList<Object> toMutableList(@Nonnull String json) throws JsonParseException {
//		try {
//			return newFastList(
//					// JSONArray实现List接口, 转换为MutableList
//					parseArray(json));
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param <T>
//	 * @param json
//	 * @param type
//	 * @return
//	 * @throws JsonParseException
//	 */
//	public static final <T> MutableList<T> toMutableList(@Nonnull String json, Class<T> type)
//			throws JsonParseException {
//		try {
//			return newFastList(
//					// JSONArray实现List接口, 转换为MutableList
//					parseArray(json, type));
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param json
//	 * @return
//	 * @throws JsonParseException
//	 */
//	public static final ImmutableList<Object> toImmutableList(@Nonnull String json) throws JsonParseException {
//		try {
//			return newImmutableList(
//					// JSONArray实现List接口, 转换为ImmutableList
//					parseArray(json));
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param <T>
//	 * @param json
//	 * @param type
//	 * @return
//	 * @throws JsonParseException
//	 */
//	public static final <T> ImmutableList<T> toImmutableList(@Nonnull String json, Class<T> type)
//			throws JsonParseException {
//		try {
//			return newImmutableList(
//					// JSONArray实现List接口, 转换为ImmutableList
//					parseArray(json, type));
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param json
//	 * @return
//	 * @throws JsonParseException
//	 */
//	public static final Map<String, Object> toMap(@Nonnull String json) throws JsonParseException {
//		try {
//			return parseObject(json);
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param json
//	 * @return
//	 * @throws JsonParseException
//	 */
//	public static final MutableMap<String, Object> toMutableMap(@Nonnull String json) throws JsonParseException {
//		try {
//			return newUnifiedMap(
//					// JSONObject实现Map接口, 转换为MutableMap
//					parseObject(json));
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param json
//	 * @return
//	 * @throws JsonParseException
//	 */
//	public static final ImmutableMap<String, Object> toImmutableMap(@Nonnull String json) throws JsonParseException {
//		try {
//			return newImmutableMap(
//					// JSONObject实现Map接口, 转换为ImmutableMap
//					parseObject(json));
//		} catch (Exception e) {
//			throw new JsonParseException(json, e);
//		}
//	}
//
//	public static void main(String[] args) {
//		Map<String, String> map = new HashMap<>();
//		map.put("A", "1");
//		map.put("B", "2");
//		map.put("C", "11");
//		map.put("D", null);
//		map.put("E", null);
//
//		System.out.println(toJson(null));
//		System.out.println(toJsonHasNulls(null));
//		System.out.println(toJson(map));
//		System.out.println(toJsonHasNulls(map));
//		System.out.println(JSON.toJSONString(map));
//
//	}
//
//}
