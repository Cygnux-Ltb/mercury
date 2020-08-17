package io.mercury.common.param.map;

import static io.mercury.common.util.Assertor.nonEmpty;
import static io.mercury.common.util.Assertor.nonNull;
import static io.mercury.common.util.Assertor.requiredLength;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.System.out;

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

	private final ImmutableMap<K, String> immutableMap;

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
		MutableMap<K, String> mutableMap = MutableMaps.newUnifiedMap();
		for (K key : keys) {
			if (map.containsKey(key.kname()))
				mutableMap.put(key, map.get(key.kname()).toString());
		}
		this.immutableMap = mutableMap.toImmutable();
	}

	/**
	 * 根据传入的Key获取Map中的相应字段
	 * 
	 * @param keys
	 * @param maps
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ImmutableParamMap(@Nonnull K[] keys, @Nonnull Properties prop)
			throws NullPointerException, IllegalArgumentException {
		requiredLength(keys, 1, "keys");
		nonNull(prop, "prop");
		MutableMap<K, String> tempMap = MutableMaps.newUnifiedMap();
		for (K key : keys) {
			if (prop.containsKey(key.kname()))
				tempMap.put(key, prop.get(key.kname()).toString());
		}
		this.immutableMap = tempMap.toImmutable();
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
		return parseBoolean(nonNull(immutableMap.get(key), key.kname()));
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
		return parseInt(nonNull(immutableMap.get(key), key.kname()));
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
		return parseDouble(nonNull(immutableMap.get(key), key.kname()));
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
		return nonNull(immutableMap.get(key), key.kname());
	}

	public void printParam() {
		printParam(null);
	}

	public void printParam(Logger log) {
		if (log == null)
			immutableMap
					.forEachKeyValue((key, value) -> out.println("key -> " + key.kname() + ", value -> " + value));
		else
			immutableMap.forEachKeyValue((key, value) -> log.info("key -> {}, value -> {}", key.kname(), value));
	}

}
