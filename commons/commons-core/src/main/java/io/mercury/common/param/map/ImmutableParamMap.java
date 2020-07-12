package io.mercury.common.param.map;

import static io.mercury.common.util.Assertor.nonEmpty;
import static io.mercury.common.util.Assertor.nonNull;
import static io.mercury.common.util.Assertor.requiredLength;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.Double.parseDouble;

import java.util.Map;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.slf4j.Logger;

import io.mercury.common.collections.MutableMaps;
import io.mercury.common.param.ParamKey;
import io.mercury.common.param.ParamType;

public final class ImmutableParamMap<K extends ParamKey> {

	private final ImmutableMap<K, String> paramMap;

	/**
	 * 根据传入的Key获取Map中的相应字段
	 * 
	 * @param keys
	 * @param map
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ImmutableParamMap(@Nonnull K[] keys, @Nonnull Map<?, ?> map)
			throws NullPointerException, IllegalArgumentException {
		requiredLength(keys, 1, "keys");
		nonEmpty(map, "map");
		MutableMap<K, String> tempMap = MutableMaps.newUnifiedMap();
		for (K key : keys) {
			if (map.containsKey(key.key())) {
				tempMap.put(key, map.get(key.key()).toString());
			}
		}
		this.paramMap = tempMap.toImmutable();
	}

	/**
	 * 根据传入的Key获取Map中的相应字段
	 * 
	 * @param keys
	 * @param maps
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ImmutableParamMap(@Nonnull K[] keys, @Nonnull Properties properties)
			throws NullPointerException, IllegalArgumentException {
		requiredLength(keys, 1, "keys");
		nonNull(properties, "properties");
		MutableMap<K, String> tempMap = MutableMaps.newUnifiedMap();
		for (K key : keys) {
			if (properties.containsKey(key.key()))
				tempMap.put(key, properties.get(key.key()).toString());
		}
		this.paramMap = tempMap.toImmutable();
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 */
	public boolean getBoolean(K key) throws IllegalArgumentException, NullPointerException {
		if (key.type() != ParamType.BOOLEAN)
			throw new IllegalArgumentException("Key -> " + key + " ParamType is not BOOLEAN. paramType==" + key.type());
		return parseBoolean(nonNull(paramMap.get(key), key.key()));
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 * @throws NumberFormatException
	 */
	public int getInt(K key) throws IllegalArgumentException, NullPointerException, NumberFormatException {
		if (key.type() != ParamType.INT)
			throw new IllegalArgumentException("Key -> " + key + " ParamType is not [INT]. paramType==" + key.type());
		return parseInt(nonNull(paramMap.get(key), key.key()));
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 * @throws NumberFormatException
	 */
	public double getDouble(K key) throws IllegalArgumentException, NullPointerException, NumberFormatException {
		if (key.type() != ParamType.DOUBLE)
			throw new IllegalArgumentException(
					"Key -> " + key + " ParamType is not [DOUBLE], paramType==" + key.type());
		return parseDouble(nonNull(paramMap.get(key), key.key()));
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 */
	public String getString(K key) throws IllegalArgumentException, NullPointerException {
		if (key.type() != ParamType.STRING)
			throw new IllegalArgumentException(
					"Key -> " + key + " ParamType is not [STRING], paramType==" + key.type());
		return nonNull(paramMap.get(key), key.key());
	}

	public void printParam() {
		printParam(null);
	}

	public void printParam(Logger log) {
		if (log == null)
			paramMap.forEachKeyValue((key, value) -> System.out.println("key -> " + key.key() + ", value -> " + value));
		else
			paramMap.forEachKeyValue((key, value) -> log.info("key -> {}, value -> {}", key.key(), value));
	}

}
