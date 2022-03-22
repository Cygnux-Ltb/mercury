package io.mercury.common.concurrent.map;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MapUtil;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashMapLong;

import java.util.concurrent.ConcurrentMap;

public final class JctConcurrentMaps {

    /**
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ConcurrentMap<K, V> newNonBlockingHashMap() {
        return new NonBlockingHashMap<>();
    }

    /**
     * @param <K>
     * @param <V>
     * @param capacity
     * @return
     */
    public static <K, V> ConcurrentMap<K, V> newNonBlockingHashMap(int capacity) {
        return new NonBlockingHashMap<>(MapUtil.optimizationCapacity(8, capacity));
    }

    /**
     * @param capacity
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ConcurrentMap<K, V> newNonBlockingHashMap(Capacity capacity) {
        return new NonBlockingHashMap<>(Capacity.checkAndGet(capacity));
    }

    /**
     * @param <V>
     * @return
     */
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap() {
        return new NonBlockingHashMapLong<>();
    }

    /**
     * @param <V>
     * @param capacity
     * @return
     */
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap(int capacity) {
        return new NonBlockingHashMapLong<>(MapUtil.optimizationCapacity(8, capacity));
    }

    /**
     * @param capacity
     * @param <V>
     * @return
     */
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap(Capacity capacity) {
        return new NonBlockingHashMapLong<>(Capacity.checkAndGet(capacity));
    }

    /**
     * @param <V>
     * @param spaceOptimization
     * @return
     */
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap(boolean spaceOptimization) {
        return new NonBlockingHashMapLong<>(spaceOptimization);
    }

    /**
     * @param <V>
     * @param capacity
     * @param spaceOptimization
     * @return
     */
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap(int capacity, boolean spaceOptimization) {
        return new NonBlockingHashMapLong<>(MapUtil.optimizationCapacity(8, capacity), spaceOptimization);
    }

    /**
     * @param capacity
     * @param spaceOptimization
     * @param <V>
     * @return
     */
    public static <V> NonBlockingHashMapLong<V> newNonBlockingLongMap(Capacity capacity, boolean spaceOptimization) {
        return new NonBlockingHashMapLong<>(Capacity.checkAndGet(capacity), spaceOptimization);
    }

}
