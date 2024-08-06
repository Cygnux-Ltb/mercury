package io.mercury.common.concurrent.map;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MapUtil;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashMapLong;

import java.util.concurrent.ConcurrentMap;

public final class JctConcurrentMaps {

    /**
     * @param <K> Key type
     * @param <V> Value type
     * @return ConcurrentMap<K, V>
     */
    public static <K, V> ConcurrentMap<K, V> newNonBlockingHashMap() {
        return new NonBlockingHashMap<>();
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param capacity int
     * @return ConcurrentMap<K, V>
     */
    public static <K, V> ConcurrentMap<K, V> newNonBlockingHashMap(int capacity) {
        return new NonBlockingHashMap<>(MapUtil.optimizationCapacity(8, capacity));
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param capacity Capacity
     * @return ConcurrentMap<K, V>
     */
    public static <K, V> ConcurrentMap<K, V> newNonBlockingHashMap(Capacity capacity) {
        return new NonBlockingHashMap<>(Capacity.checkAndGet(capacity));
    }

    /**
     * @param <V> Value type
     * @return NonBlockingHashMapLong<V>
     */
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap() {
        return new NonBlockingHashMapLong<>();
    }

    /**
     * @param <V>      Value type
     * @param capacity int
     * @return NonBlockingHashMapLong<V>
     */
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap(int capacity) {
        return new NonBlockingHashMapLong<>(MapUtil.optimizationCapacity(8, capacity));
    }

    /**
     * @param <V>      Value type
     * @param capacity Capacity
     * @return NonBlockingHashMapLong<V>
     */
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap(Capacity capacity) {
        return new NonBlockingHashMapLong<>(Capacity.checkAndGet(capacity));
    }

    /**
     * @param <V>               Value type
     * @param spaceOptimization boolean
     * @return NonBlockingHashMapLong<V>
     */
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap(boolean spaceOptimization) {
        return new NonBlockingHashMapLong<>(spaceOptimization);
    }

    /**
     * @param <V>               Value type
     * @param capacity          int
     * @param spaceOptimization boolean
     * @return NonBlockingHashMapLong<V>
     */
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap(int capacity,
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
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap(Capacity capacity,
                                                                      boolean spaceOptimization) {
        return new NonBlockingHashMapLong<>(Capacity.checkAndGet(capacity),
                spaceOptimization);
    }

}
