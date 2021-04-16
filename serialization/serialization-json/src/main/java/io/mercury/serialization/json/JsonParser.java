package io.mercury.serialization.json;

import static com.google.gson.JsonParser.parseString;
import static io.mercury.common.collections.ImmutableLists.newImmutableList;
import static io.mercury.common.collections.ImmutableMaps.newImmutableMap;
import static io.mercury.common.collections.MutableLists.newFastList;
import static io.mercury.common.collections.MutableMaps.newUnifiedMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.JsonElement;

public final class JsonParser {

	private static final ObjectMapper Mapper = new ObjectMapper()
	// TODO 添加反序列化属性
	;

	private static final TypeFactory TypeFactory = Mapper.getTypeFactory()
	// TODO 添加配置信息
	;

	/**
	 * 
	 * @param json
	 * @return
	 */
	public static JsonElement parseJson(@Nonnull String json) {
		return parseString(json);
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public static boolean isJsonArray(@Nonnull String json) {
		return parseString(json).isJsonArray();
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public static boolean isJsonObject(@Nonnull String json) {
		return parseString(json).isJsonObject();
	}

	/**
	 * 
	 * @param <T>
	 * @param json
	 * @return
	 * @throws JsonParseException
	 */
	@Nullable
	public static final <T> T toObject(@Nonnull String json) throws JsonParseException {
		try {
			if (json == null)
				return null;
			return Mapper.readValue(json, new TypeReference<T>() {
			});
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 */
	@Nullable
	public static final <T> T toObject(@Nonnull String json, @Nonnull Class<T> clazz) throws JsonParseException {
		try {
			if (json == null || clazz == null)
				return null;
			return Mapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws JsonParseException
	 */
	public static final <T> List<T> toList(@Nonnull String json) throws JsonParseException {
		try {
			return Mapper.readValue(json, new TypeReference<List<T>>() {
			});
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 */
	public static final <T> List<T> toList(@Nonnull String json, @Nonnull Class<T> clazz) throws JsonParseException {
		try {
			return Mapper.readValue(json, TypeFactory.constructCollectionLikeType(List.class, clazz));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws JsonParseException
	 */
	public static final <T> MutableList<T> toMutableList(@Nonnull String json) throws JsonParseException {
		try {
			return newFastList(
					// List接口, 转换为MutableList
					Mapper.readValue(json, new TypeReference<List<T>>() {
					}));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 */
	public static final <T> MutableList<T> toMutableList(@Nonnull String json, @Nonnull Class<T> clazz)
			throws JsonParseException {
		try {
			List<T> list = Mapper.readValue(json, TypeFactory.constructCollectionLikeType(List.class, clazz));
			return newFastList(
					// List接口, 转换为MutableList
					list);
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws JsonParseException
	 */
	public static final <T> ImmutableList<T> toImmutableList(@Nonnull String json) throws JsonParseException {
		try {
			return newImmutableList(
					// List接口, 转换为MutableList
					Mapper.readValue(json, new TypeReference<List<T>>() {
					}));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 */
	public static final <T> ImmutableList<T> toImmutableList(@Nonnull String json, @Nonnull Class<T> clazz)
			throws JsonParseException {
		try {
			List<T> list = Mapper.readValue(json, TypeFactory.constructCollectionLikeType(List.class, clazz));
			return newImmutableList(
					// List接口, 转换为MutableList
					list);
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws JsonParseException
	 */
	public static final <K, V> Map<K, V> toMap(@Nonnull String json) throws JsonParseException {
		try {
			return Mapper.readValue(json, new TypeReference<Map<K, V>>() {
			});
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param json
	 * @param keyClass
	 * @param valueClass
	 * @return
	 * @throws JsonParseException
	 */
	public static final <K, V> Map<K, V> toMap(@Nonnull String json, @Nonnull Class<K> keyClass,
			@Nonnull Class<V> valueClass) throws JsonParseException {
		try {
			return Mapper.readValue(json, TypeFactory.constructMapLikeType(Map.class, keyClass, valueClass));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws JsonParseException
	 */
	public static final <K, V> MutableMap<K, V> toMutableMap(@Nonnull String json) throws JsonParseException {
		try {
			return newUnifiedMap(
					// Map接口, 转换为MutableMap
					Mapper.readValue(json, new TypeReference<Map<K, V>>() {
					}));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws JsonParseException
	 */
	public static final <K, V> ImmutableMap<K, V> toImmutableMap(@Nonnull String json) throws JsonParseException {
		try {
			return newImmutableMap(
					// JSONObject实现Map接口, 转换为ImmutableMap
					Mapper.readValue(json, new TypeReference<Map<K, V>>() {
					}));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class JsonParseException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 9000408863460789219L;

		public JsonParseException(String json, Throwable throwable) {
			super("Parse JSON -> " + json + " | Throw exception -> [" + throwable.getClass().getSimpleName() + "]",
					throwable);
		}

		public JsonParseException(Throwable throwable) {
			super("Parse JSON throw exception -> [" + throwable.getClass().getSimpleName() + "]", throwable);
		}

		public JsonParseException(String message) {
			super(message);
		}

	}

	public static void main(String[] args) {
		Map<String, String> map0 = new HashMap<>();
		map0.put("A", "1");
		map0.put("B", "2");
		map0.put("C", "11");
		map0.put("D", null);
		map0.put("E", null);
		String json = JsonWrapper.toJsonHasNulls(map0);
		System.out.println(json);
		Map<Object, Object> map1 = JsonParser.toMap(json);
		System.out.println(map1);
		map1.entrySet().forEach(obj -> {
			System.out.println(obj.getKey().getClass());
			System.out.println(obj.getValue().getClass());
		});

	}

}
