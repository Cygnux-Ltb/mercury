package io.mercury.common.collections;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import io.mercury.common.util.BitOperator;

public final class MapUtil {

	private MapUtil() {
	}

	/**
	 * 
	 * @param map0
	 * @param map1
	 * @return
	 */
	public static final boolean isEquals(Map<?, ?> map0, Map<?, ?> map1) {
		if (map0 == null && map1 == null)
			return true;
		else if ((map0 != null && map1 == null) || (map1 != null && map0 == null))
			return false;
		else
			return map0.equals(map1);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param key
	 * @param value
	 * @return
	 */
	public static final <K, V> Map<K, V> map(@Nonnull K key, @Nonnull V value) {
		var map = new HashMap<K, V>(1);
		map.put(key, value);
		return map;
	}

	/**
	 * 
	 * @param capacity
	 * @return
	 */
	public static final int optimizationCapacity(int capacity) {
		return optimizationCapacity(16, capacity);
	}

	/**
	 * 
	 * @param minCapacity
	 * @param capacity
	 * @return
	 */
	public static final int optimizationCapacity(int minCapacity, int capacity) {
		minCapacity = BitOperator.minPow2(minCapacity);
		return capacity < minCapacity ? minCapacity : BitOperator.minPow2(capacity);
	}

}
