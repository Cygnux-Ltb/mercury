package io.mercury.codec.json;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.parseObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.fastjson.JSONValidator.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.mercury.common.annotation.lang.MayThrowsRuntimeException;
import io.mercury.common.collections.ImmutableLists;
import io.mercury.common.collections.ImmutableMaps;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.collections.MutableMaps;

public final class JsonUtil {

	private static final Gson GsonNormal = new GsonBuilder().create();

	public static final String toJson(@Nonnull Object obj) {
		return GsonNormal.toJson(obj);
	}

	private static final Gson GsonHasNulls = new GsonBuilder().serializeNulls().create();

	public static final String toJsonHasNulls(@Nonnull Object obj) {
		return GsonHasNulls.toJson(obj);
	}

	public static boolean isJsonValue(String str) {
		return JSONValidator.from(str).getType() == Type.Value;
	}

	public static boolean isJsonArray(String str) {
		return JSONValidator.from(str).getType() == Type.Array;
	}

	public static boolean isJsonObject(String str) {
		return JSONValidator.from(str).getType() == Type.Object;
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final JSONObject toJsonObject(@Nonnull String json) {
		try {
			return parseObject(json);
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final JSONArray toJsonArray(@Nonnull String json) {
		try {
			return parseArray(json);
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final <T> T toObject(@Nonnull String json, @Nonnull Class<T> clazz) {
		try {
			return parseObject(json, clazz);
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final List<Object> toList(@Nonnull String json) {
		try {
			return parseArray(json);
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final <T> List<T> toList(@Nonnull String json, @Nonnull Class<T> clazz) {
		try {
			return parseArray(json, clazz);
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final MutableList<Object> toMutableList(@Nonnull String json) {
		try {
			return MutableLists.newFastList(
					// JSONArray实现List接口, 转换为MutableList
					parseArray(json));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final ImmutableList<Object> toImmutableList(@Nonnull String json) {
		try {
			return ImmutableLists.newList(
					// JSONArray实现List接口, 转换为ImmutableList
					parseArray(json));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final Map<String, Object> toMap(@Nonnull String json) {
		try {
			return parseObject(json);
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final MutableMap<String, Object> toMutableMap(@Nonnull String json) {
		try {
			return MutableMaps.newUnifiedMap(
					// JSONObject实现Map接口, 转换为MutableMap
					parseObject(json));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final ImmutableMap<String, Object> toImmutableMap(@Nonnull String json) {
		try {
			return ImmutableMaps.newMap(
					// JSONObject实现Map接口, 转换为ImmutableMap
					parseObject(json));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
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
