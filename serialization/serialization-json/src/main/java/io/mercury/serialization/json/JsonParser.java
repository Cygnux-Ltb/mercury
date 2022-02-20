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
import com.google.gson.JsonSyntaxException;

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
	public static JsonElement parseJson(@Nonnull final String json) throws JsonSyntaxException {
		return parseString(json);
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public static boolean isJsonArray(@Nonnull final String json) throws JsonSyntaxException {
		return parseString(json).isJsonArray();
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public static boolean isJsonObject(@Nonnull final String json) throws JsonSyntaxException {
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
	public static <T> T toObject(@Nullable final String json) throws JsonParseException {
		try {
			if (json == null || json.isEmpty())
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
	 * @param type
	 * @return
	 * @throws JsonParseException
	 */
	@Nullable
	public static <T> T toObject(@Nullable final String json, @Nullable Class<T> type) throws JsonParseException {
		try {
			if (json == null || json.isEmpty() || type == null)
				return null;
			return Mapper.readValue(json, type);
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
	public static <T> List<T> toList(@Nonnull String json) throws JsonParseException {
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
	 * @param type
	 * @return
	 * @throws JsonParseException
	 */
	public static <T> List<T> toList(@Nonnull String json, @Nonnull Class<T> type) throws JsonParseException {
		try {
			return Mapper.readValue(json, TypeFactory.constructCollectionLikeType(List.class, type));
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
	public static <T> MutableList<T> toMutableList(@Nonnull String json) throws JsonParseException {
		try {
			return newFastList(
					// List convert to MutableList
					Mapper.readValue(json, new TypeReference<List<T>>() {
					}));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param json
	 * @param type
	 * @return
	 * @throws JsonParseException
	 */
	public static <T> MutableList<T> toMutableList(@Nonnull String json, @Nonnull Class<T> type)
			throws JsonParseException {
		try {
			final List<T> list = Mapper.readValue(json, TypeFactory.constructCollectionLikeType(List.class, type));
			return newFastList(
					// List convert to MutableList
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
	public static <T> ImmutableList<T> toImmutableList(@Nonnull String json) throws JsonParseException {
		try {
			return newImmutableList(
					// List convert to MutableList
					Mapper.readValue(json, new TypeReference<List<T>>() {
					}));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @param json
	 * @param type
	 * @return
	 * @throws JsonParseException
	 */
	public static <T> ImmutableList<T> toImmutableList(@Nonnull String json, @Nonnull Class<T> type)
			throws JsonParseException {
		try {
			final List<T> list = Mapper.readValue(json, TypeFactory.constructCollectionLikeType(List.class, type));
			return newImmutableList(
					// List convert to MutableList
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
	public static <K, V> Map<K, V> toMap(@Nonnull final String json) throws JsonParseException {
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
	 * @param keyType
	 * @param valueType
	 * @return
	 * @throws JsonParseException
	 */
	public static <K, V> Map<K, V> toMap(@Nonnull final String json, @Nonnull Class<K> keyType,
			@Nonnull Class<V> valueType) throws JsonParseException {
		try {
			return Mapper.readValue(json, TypeFactory.constructMapLikeType(Map.class, keyType, valueType));
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
	public static <K, V> MutableMap<K, V> toMutableMap(@Nonnull final String json) throws JsonParseException {
		try {
			return newUnifiedMap(
					// Map convert to MutableMap
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
	public static <K, V> ImmutableMap<K, V> toImmutableMap(@Nonnull String json) throws JsonParseException {
		try {
			return newImmutableMap(
					// Map convert to ImmutableMap
					Mapper.readValue(json, new TypeReference<Map<K, V>>() {
					}));
		} catch (Exception e) {
			throw new JsonParseException(json, e);
		}
	}

	/**
	 * 
	 * @author yellow013
	 */
	public static final class JsonParseException extends RuntimeException {

		private static final long serialVersionUID = 9000408863460789219L;

		public JsonParseException(String json, Throwable cause) {
			super("Parsing JSON -> " + json + " , Throw exception -> [" + cause.getClass().getName() + "]", cause);
		}

		public JsonParseException(Throwable cause) {
			super("Parsing JSON throw exception -> [" + cause.getClass().getName() + "]", cause);
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
		map1.forEach((key, value) -> {
			System.out.println(key.getClass());
			System.out.println(value.getClass());
		});
	}

}
