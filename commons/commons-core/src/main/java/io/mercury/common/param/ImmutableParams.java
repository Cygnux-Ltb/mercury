package io.mercury.common.param;

import static io.mercury.common.util.Assertor.nonEmpty;
import static io.mercury.common.util.Assertor.nonNull;
import static io.mercury.common.util.Assertor.requiredLength;
import static java.lang.System.out;

import java.util.Map;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.slf4j.Logger;

import io.mercury.common.collections.MutableMaps;

public final class ImmutableParams<K extends ParamKey> {

	private final ImmutableMap<K, String> savedParams;

	/**
	 * 根据传入的Key获取Map中的相应字段
	 * 
	 * @param keys
	 * @param map
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ImmutableParams(@Nonnull K[] keys, @Nonnull Map<?, ?> map)
			throws NullPointerException, IllegalArgumentException {
		requiredLength(keys, 1, "keys");
		nonEmpty(map, "map");
		MutableMap<K, String> tempMap = MutableMaps.newUnifiedMap();
		for (K key : keys) {
			if (map.containsKey(key.keyName()))
				tempMap.put(key, map.get(key.keyName()).toString());
		}
		this.savedParams = tempMap.toImmutable();
	}

	/**
	 * 根据传入的Key获取Properties中的相应字段
	 * 
	 * @param keys
	 * @param maps
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ImmutableParams(@Nonnull K[] keys, @Nonnull Properties prop)
			throws NullPointerException, IllegalArgumentException {
		requiredLength(keys, 1, "keys");
		nonNull(prop, "prop");
		MutableMap<K, String> tempMap = MutableMaps.newUnifiedMap();
		for (K key : keys) {
			if (prop.containsKey(key.keyName()))
				tempMap.put(key, prop.get(key.keyName()).toString());
		}
		this.savedParams = tempMap.toImmutable();
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
		return Boolean.parseBoolean(nonNull(savedParams.get(key), key.keyName()));
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
		return Integer.parseInt(nonNull(savedParams.get(key), key.keyName()));
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
		return Double.parseDouble(nonNull(savedParams.get(key), key.keyName()));
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
		return nonNull(savedParams.get(key), key.keyName());
	}

	public void printParam() {
		printParam(null);
	}

	public void printParam(Logger log) {
		if (log == null)
			savedParams.forEachKeyValue((key, value) -> out.println("key -> " + key.keyName() + ", value -> " + value));
		else
			savedParams.forEachKeyValue((key, value) -> log.info("key -> {}, value -> {}", key.keyName(), value));
	}

}
