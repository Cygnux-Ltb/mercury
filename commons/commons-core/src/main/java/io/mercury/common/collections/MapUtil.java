package io.mercury.common.collections;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import io.mercury.common.util.BitOperator;

public final class MapUtil {

    private MapUtil() {
    }

    /**
     * @param map0
     * @param map1
     * @return
     */
    public static boolean isEquals(Map<?, ?> map0, Map<?, ?> map1) {
        if (map0 == map1)
            return true;
        else if (map0 == null)
            return false;
        else if (map1 == null)
            return false;
        else
            return map0.equals(map1);
    }

    /**
     * @param <K>
     * @param <V>
     * @param key
     * @param value
     * @return
     */
    public static <K, V> Map<K, V> map(@Nonnull K key, @Nonnull V value) {
        Map<K, V> map = new HashMap<>(1);
        map.put(key, value);
        return map;
    }

    /**
     * @param capacity
     * @return
     */
    public static int optimizationCapacity(int capacity) {
        return optimizationCapacity(Capacity.DEFAULT_SIZE, capacity);
    }

    /**
     * @param minCapacity
     * @param capacity
     * @return
     */
    public static int optimizationCapacity(int minCapacity, int capacity) {
        minCapacity = BitOperator.minPow2(minCapacity);
        return capacity < minCapacity ? minCapacity : BitOperator.minPow2(capacity);
    }

}
