package io.mercury.common.param;

import static io.mercury.common.util.Assertor.nonEmptyMap;
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

public final class ImmutableParams<K extends ParamKey> implements Params<K> {

	private final ImmutableMap<K, String> params;

	/**
	 * 根据传入的Key获取Map中的相应字段
	 * 
	 * @param keys
	 * @param map
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	@SafeVarargs
	public ImmutableParams(@Nonnull Map<?, ?> map, @Nonnull K... keys)
			throws NullPointerException, IllegalArgumentException {
		requiredLength(keys, 1, "keys");
		nonEmptyMap(map, "map");
		MutableMap<K, String> mutableMap = MutableMaps.newUnifiedMap();
		for (K key : keys) {
			if (map.containsKey(key.paramName()))
				mutableMap.put(key, map.get(key.paramName()).toString());
		}
		this.params = mutableMap.toImmutable();
	}

	/**
	 * 根据传入的Key获取Properties中的相应字段
	 * 
	 * @param keys
	 * @param maps
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	@SafeVarargs
	public ImmutableParams(@Nonnull Properties prop, @Nonnull K... keys)
			throws NullPointerException, IllegalArgumentException {
		requiredLength(keys, 1, "keys");
		nonNull(prop, "prop");
		MutableMap<K, String> mutableMap = MutableMaps.newUnifiedMap();
		for (K key : keys) {
			if (prop.containsKey(key.paramName()))
				mutableMap.put(key, prop.get(key.paramName()).toString());
		}
		this.params = mutableMap.toImmutable();
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 */
	@Override
	public boolean getBoolean(K key) throws IllegalArgumentException, NullPointerException {
		if (key.type() != ParamType.BOOLEAN)
			throw new IllegalArgumentException("Key -> " + key + " ParamType is not BOOLEAN, paramType==" + key.type());
		return Boolean.parseBoolean(nonNull(params.get(key), key.paramName()));
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 * @throws NumberFormatException
	 */
	@Override
	public int getInt(K key) throws IllegalArgumentException, NullPointerException, NumberFormatException {
		if (key.type() != ParamType.INT)
			throw new IllegalArgumentException("Key -> " + key + " ParamType is not [INT]. paramType==" + key.type());
		return Integer.parseInt(nonNull(params.get(key), key.paramName()));
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 * @throws NumberFormatException
	 */
	@Override
	public double getDouble(K key) throws IllegalArgumentException, NullPointerException, NumberFormatException {
		if (key.type() != ParamType.DOUBLE)
			throw new IllegalArgumentException(
					"Key -> " + key + " ParamType is not [DOUBLE], paramType==" + key.type());
		return Double.parseDouble(nonNull(params.get(key), key.paramName()));
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 */
	@Override
	public String getString(K key) throws IllegalArgumentException, NullPointerException {
		if (key.type() != ParamType.STRING)
			throw new IllegalArgumentException(
					"Key -> " + key + " ParamType is not [STRING], paramType==" + key.type());
		return nonNull(params.get(key), key.paramName());
	}

	public void printParams() {
		printParams(null);
	}

	public void printParams(Logger log) {
		if (log == null) {
			params.forEachKeyValue((key, value) -> out
					.println("Param id==" + key.id() + ", paramName -> " + key.paramName() + ", value -> " + value));
		} else {
			params.forEachKeyValue((key, value) -> log.info("Param id=={}, paramName -> {}, value -> {}", key.id(),
					key.paramName(), value));
		}
	}

}
