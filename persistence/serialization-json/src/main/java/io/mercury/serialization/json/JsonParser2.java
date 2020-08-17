package io.mercury.serialization.json;

import static io.mercury.common.collections.ImmutableLists.newImmutableList;
import static io.mercury.common.collections.ImmutableMaps.newImmutableMap;
import static io.mercury.common.collections.MutableLists.newFastList;
import static io.mercury.common.collections.MutableMaps.newUnifiedMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public final class JsonParser2 {

	private static final ObjectMapper Mapper = new ObjectMapper()
	// 添加反序列化属性
	;

	private static final TypeFactory TypeFactory = Mapper.getTypeFactory()
	// 添加配置信息
	;

	/**
	 * 
	 * @param <T>
	 * @param json
	 * @return
	 * @throws JsonParseException
	 */
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
	public static final <K, V> Map<K, V> toMap(@Nonnull String json, Class<K> keyClass, Class<V> valueClass)
			throws JsonParseException {
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

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<>();
		map.put("A", "1");
		map.put("B", "2");
		map.put("C", "11");
		map.put("D", null);
		map.put("E", null);
		System.out.println();
	}

}
