package io.mercury.common.concurrent.map;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MapUtil;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashMapLong;

public final class NonBlockingMaps {

    /**
     * @param <K> Key type
     * @param <V> Value type
     * @return ConcurrentMap<K, V>
     */
    public static <K, V> NonBlockingHashMap<K, V> newHashMap() {
        return new NonBlockingHashMap<>();
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param capacity int
     * @return ConcurrentMap<K, V>
     */
    public static <K, V> NonBlockingHashMap<K, V> newHashMap(int capacity) {
        return new NonBlockingHashMap<>(MapUtil.optimizationCapacity(8, capacity));
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param capacity Capacity
     * @return ConcurrentMap<K, V>
     */
    public static <K, V> NonBlockingHashMap<K, V> newHashMap(Capacity capacity) {
        return new NonBlockingHashMap<>(Capacity.checkAndGet(capacity));
    }

    /**
     * @param <V> Value type
     * @return NonBlockingHashMapLong<V>
     */
    public static <V> NonBlockingHashMapLong<V> newLongHashMap() {
        return new NonBlockingHashMapLong<>();
    }

    /**
     * @param <V>      Value type
     * @param capacity int
     * @return NonBlockingHashMapLong<V>
     */
    public static <V> NonBlockingHashMapLong<V> newLongHashMap(int capacity) {
        return new NonBlockingHashMapLong<>(MapUtil.optimizationCapacity(8, capacity));
    }

    /**
     * @param <V>      Value type
     * @param capacity Capacity
     * @return NonBlockingHashMapLong<V>
     */
    public static <V> NonBlockingHashMapLong<V> newLongHashMap(Capacity capacity) {
        return new NonBlockingHashMapLong<>(Capacity.checkAndGet(capacity));
    }

    /**
     * @param <V>               Value type
     * @param spaceOptimization boolean
     * @return NonBlockingHashMapLong<V>
     */
    public static <V> NonBlockingHashMapLong<V> newLongHashMap(boolean spaceOptimization) {
        return new NonBlockingHashMapLong<>(spaceOptimization);
    }

    /**
     * @param <V>               Value type
     * @param capacity          int
     * @param spaceOptimization boolean
     * @return NonBlockingHashMapLong<V>
     */
    public static <V> NonBlockingHashMapLong<V> newLongHashMap(int capacity,
                                                               boolean spaceOptimization) {
        return new NonBlockingHashMapLong<>(MapUtil.optimizationCapacity(8, capacity),
                spaceOptimization);
    }

    /**
     * @param <V>               Value type
     * @param capacity          Capacity
     * @param spaceOptimization boolean
     * @return NonBlockingHashMapLong<V>
     */
    public static <V> NonBlockingHashMapLong<V> newLongHashMap(Capacity capacity,
                                                               boolean spaceOptimization) {
        return new NonBlockingHashMapLong<>(Capacity.checkAndGet(capacity),
                spaceOptimization);
    }

}
