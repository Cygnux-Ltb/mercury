package io.mercury.common.concurrent.map;

import java.util.concurrent.ConcurrentMap;

import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashMapLong;

public final class JctConcurrentMaps {

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
	 * @param capacity
	 * @return
	 */
	public static <K, V> ConcurrentMap<K, V> newNonBlockingHashMap(int capacity) {
		return new NonBlockingHashMap<>(capacity);
	}

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newNonBlockingLongMap() {
		return new NonBlockingHashMapLong<>();
	}

	/**
	 * 
	 * @param <V>
	 * @param capacity
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newNonBlockingLongMap(int capacity) {
		return new NonBlockingHashMapLong<>(capacity);
	}

	/**
	 * 
	 * @param <V>
	 * @param spaceOptimization
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newNonBlockingLongMap(boolean spaceOptimization) {
		return new NonBlockingHashMapLong<>(spaceOptimization);
	}

	/**
	 * 
	 * @param <V>
	 * @param capacity
	 * @param spaceOptimization
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newNonBlockingLongMap(int capacity, boolean spaceOptimization) {
		return new NonBlockingHashMapLong<>(capacity, spaceOptimization);
	}

}
