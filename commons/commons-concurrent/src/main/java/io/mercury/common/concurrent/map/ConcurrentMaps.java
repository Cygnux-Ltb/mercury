package io.mercury.common.concurrent.map;

import java.util.concurrent.ConcurrentMap;

import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashMapLong;

public final class ConcurrentMaps {

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newLongNonBlockingHashMap() {
		return new NonBlockingHashMapLong<>();
	}

	/**
	 * 
	 * @param <V>
	 * @param capacity
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newLongNonBlockingHashMap(final int initCapacity) {
		return new NonBlockingHashMapLong<>(initCapacity);
	}

	/**
	 * 
	 * @param <V>
	 * @param spaceOptimization
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newLongNonBlockingHashMap(final boolean spaceOptimization) {
		return new NonBlockingHashMapLong<>(spaceOptimization);
	}

	/**
	 * 
	 * @param <V>
	 * @param initCapacity
	 * @param spaceOptimization
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newLongNonBlockingHashMap(final int initCapacity,
			final boolean spaceOptimization) {
		return new NonBlockingHashMapLong<>(initCapacity, spaceOptimization);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> ConcurrentMap<K, V> newNonBlockingHashMap() {
		return new NonBlockingHashMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param initCapacity
	 * @return
	 */
	public static <K, V> ConcurrentMap<K, V> newNonBlockingHashMap(final int initCapacity) {
		return new NonBlockingHashMap<>(initCapacity);
	}

}
