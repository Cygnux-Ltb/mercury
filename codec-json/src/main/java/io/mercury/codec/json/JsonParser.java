package io.mercury.codec.json;

import static com.alibaba.fastjson.JSON.isValidArray;
import static com.alibaba.fastjson.JSON.isValidObject;
import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.parseObject;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.mercury.common.annotation.lang.MayThrowsRuntimeException;
import io.mercury.common.collections.ImmutableLists;
import io.mercury.common.collections.ImmutableMaps;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.collections.MutableMaps;

public final class JsonParser {

	public static boolean isJsonObject(String str) {
		return isValidObject(str);
	}

	public static boolean isJsonArray(String str) {
		return isValidArray(str);
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final JSONObject toJSONObject(@Nonnull String json) {
		try {
			return parseObject(json);
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	@MayThrowsRuntimeException(JsonParseException.class)
	public static final JSONArray toJSONArray(@Nonnull String json) {
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
	public static final <T> List<T> toList(@Nonnull String json, @Nonnull Class<T> clazz) {
		try {
			return parseArray(json, clazz);
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

}
