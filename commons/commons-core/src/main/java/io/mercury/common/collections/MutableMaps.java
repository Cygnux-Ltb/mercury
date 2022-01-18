package io.mercury.common.collections;

import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.collections.api.bimap.MutableBiMap;
import org.eclipse.collections.api.map.ConcurrentMutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.MutableDoubleBooleanMap;
import org.eclipse.collections.api.map.primitive.MutableDoubleIntMap;
import org.eclipse.collections.api.map.primitive.MutableDoubleLongMap;
import org.eclipse.collections.api.map.primitive.MutableIntBooleanMap;
import org.eclipse.collections.api.map.primitive.MutableIntDoubleMap;
import org.eclipse.collections.api.map.primitive.MutableIntIntMap;
import org.eclipse.collections.api.map.primitive.MutableIntLongMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.map.primitive.MutableLongBooleanMap;
import org.eclipse.collections.api.map.primitive.MutableLongDoubleMap;
import org.eclipse.collections.api.map.primitive.MutableLongIntMap;
import org.eclipse.collections.api.map.primitive.MutableLongLongMap;
import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;
import org.eclipse.collections.api.map.primitive.MutableObjectBooleanMap;
import org.eclipse.collections.api.map.primitive.MutableObjectDoubleMap;
import org.eclipse.collections.api.map.primitive.MutableObjectIntMap;
import org.eclipse.collections.api.map.primitive.MutableObjectLongMap;
import org.eclipse.collections.api.map.sorted.MutableSortedMap;
import org.eclipse.collections.impl.bimap.mutable.HashBiMap;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMapUnsafe;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.map.mutable.primitive.DoubleBooleanHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.DoubleIntHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.DoubleLongHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntBooleanHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntDoubleHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntLongHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongBooleanHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongDoubleHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongLongHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectBooleanHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectDoubleHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectIntHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectLongHashMap;
import org.eclipse.collections.impl.map.sorted.mutable.TreeSortedMap;

public final class MutableMaps {

	private MutableMaps() {
	}

	/********** Key -> int **********/
	/**
	 * 
	 * @return MutableIntIntMap
	 */
	public static final MutableIntIntMap newIntIntHashMap() {
		return new IntIntHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntIntMap
	 */
	public static final MutableIntIntMap newIntIntHashMap(int capacity) {
		return new IntIntHashMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntIntMap
	 */
	public static final MutableIntIntMap newIntIntHashMap(Capacity capacity) {
		return new IntIntHashMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @return MutableIntLongMap
	 */
	public static final MutableIntLongMap newIntLongHashMap() {
		return new IntLongHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntLongMap
	 */
	public static final MutableIntLongMap newIntLongHashMap(int capacity) {
		return new IntLongHashMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntLongMap
	 */
	public static final MutableIntLongMap newIntLongHashMap(Capacity capacity) {
		return new IntLongHashMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @return MutableIntDoubleMap
	 */
	public static final MutableIntDoubleMap newIntDoubleHashMap() {
		return new IntDoubleHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntDoubleMap
	 */
	public static final MutableIntDoubleMap newIntDoubleHashMap(int capacity) {
		return new IntDoubleHashMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntDoubleMap
	 */
	public static final MutableIntDoubleMap newIntDoubleHashMap(Capacity capacity) {
		return new IntDoubleHashMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @return MutableIntBooleanMap
	 */
	public static final MutableIntBooleanMap newIntBooleanHashMap() {
		return new IntBooleanHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntBooleanMap
	 */
	public static final MutableIntBooleanMap newIntBooleanHashMap(int capacity) {
		return new IntBooleanHashMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntBooleanMap
	 */
	public static final MutableIntBooleanMap newIntBooleanHashMap(Capacity capacity) {
		return new IntBooleanHashMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @param <V>
	 * @return MutableIntObjectMap<V>
	 */
	public static final <V> MutableIntObjectMap<V> newIntObjectHashMap() {
		return new IntObjectHashMap<>();
	}

	/**
	 * 
	 * @param <V>
	 * @param capacity
	 * @return MutableIntObjectMap<V>
	 */
	public static final <V> MutableIntObjectMap<V> newIntObjectHashMap(int capacity) {
		return new IntObjectHashMap<>(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param <V>
	 * @param capacity
	 * @return MutableIntObjectMap<V>
	 */
	public static final <V> MutableIntObjectMap<V> newIntObjectHashMap(Capacity capacity) {
		return new IntObjectHashMap<>(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/********** Key -> long **********/
	/**
	 * 
	 * @return MutableLongLongMap
	 */
	public static final MutableLongLongMap newLongLongHashMap() {
		return new LongLongHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongLongMap
	 */
	public static final MutableLongLongMap newLongLongHashMap(int capacity) {
		return new LongLongHashMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongLongMap
	 */
	public static final MutableLongLongMap newLongLongHashMap(Capacity capacity) {
		return new LongLongHashMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @return MutableLongIntMap
	 */
	public static final MutableLongIntMap newLongIntHashMap() {
		return new LongIntHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongIntMap
	 */
	public static final MutableLongIntMap newLongIntHashMap(int capacity) {
		return new LongIntHashMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongIntMap
	 */
	public static final MutableLongIntMap newLongIntHashMap(Capacity capacity) {
		return new LongIntHashMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @return MutableLongDoubleMap
	 */
	public static final MutableLongDoubleMap newLongDoubleHashMap() {
		return new LongDoubleHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongDoubleMap
	 */
	public static final MutableLongDoubleMap newLongDoubleHashMap(int capacity) {
		return new LongDoubleHashMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongDoubleMap
	 */
	public static final MutableLongDoubleMap newLongDoubleHashMap(Capacity capacity) {
		return new LongDoubleHashMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @return MutableLongBooleanMap
	 */
	public static final MutableLongBooleanMap newLongBooleanHashMap() {
		return new LongBooleanHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongBooleanMap
	 */
	public static final MutableLongBooleanMap newLongBooleanHashMap(int capacity) {
		return new LongBooleanHashMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongBooleanMap
	 */
	public static final MutableLongBooleanMap newLongBooleanHashMap(Capacity capacity) {
		return new LongBooleanHashMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @param <V>
	 * @return MutableLongObjectMap<V>
	 */
	public static final <V> MutableLongObjectMap<V> newLongObjectHashMap() {
		return new LongObjectHashMap<>();
	}

	/**
	 * 
	 * @param <V>
	 * @param capacity
	 * @return MutableLongObjectMap<V>
	 */
	public static final <V> MutableLongObjectMap<V> newLongObjectHashMap(int capacity) {
		return new LongObjectHashMap<>(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param <V>
	 * @param capacity
	 * @return MutableLongObjectMap<V>
	 */
	public static final <V> MutableLongObjectMap<V> newLongObjectHashMap(Capacity capacity) {
		return new LongObjectHashMap<>(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/********** Key -> double **********/
	/**
	 * 
	 * @return MutableDoubleBooleanMap
	 */
	public static final MutableDoubleBooleanMap newDoubleBooleanHashMap() {
		return new DoubleBooleanHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableDoubleBooleanMap
	 */
	public static final MutableDoubleBooleanMap newDoubleBooleanHashMap(int capacity) {
		return new DoubleBooleanHashMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableDoubleBooleanMap
	 */
	public static final MutableDoubleBooleanMap newDoubleBooleanHashMap(Capacity capacity) {
		return new DoubleBooleanHashMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @return MutableDoubleIntMap
	 */
	public static final MutableDoubleIntMap newDoubleIntHashMap() {
		return new DoubleIntHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableDoubleIntMap
	 */
	public static final MutableDoubleIntMap newDoubleIntHashMap(int capacity) {
		return new DoubleIntHashMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableDoubleIntMap
	 */
	public static final MutableDoubleIntMap newDoubleIntHashMap(Capacity capacity) {
		return new DoubleIntHashMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @return MutableDoubleLongMap
	 */
	public static final MutableDoubleLongMap newDoubleLongHashMap() {
		return new DoubleLongHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableDoubleLongMap
	 */
	public static final MutableDoubleLongMap newDoubleLongHashMap(int capacity) {
		return new DoubleLongHashMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableDoubleLongMap
	 */
	public static final MutableDoubleLongMap newDoubleLongHashMap(Capacity capacity) {
		return new DoubleLongHashMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @return MutableObjectBooleanMap<K>
	 */
	public static final <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap() {
		return new ObjectBooleanHashMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectBooleanMap<K>
	 */
	public static final <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap(int capacity) {
		return new ObjectBooleanHashMap<>(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectBooleanMap<K>
	 */
	public static final <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap(Capacity capacity) {
		return new ObjectBooleanHashMap<>(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @return MutableObjectIntMap<K>
	 */
	public static final <K> MutableObjectIntMap<K> newObjectIntHashMap() {
		return new ObjectIntHashMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectIntMap<K>
	 */
	public static final <K> MutableObjectIntMap<K> newObjectIntHashMap(int capacity) {
		return new ObjectIntHashMap<>(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectIntMap<K>
	 */
	public static final <K> MutableObjectIntMap<K> newObjectIntHashMap(Capacity capacity) {
		return new ObjectIntHashMap<>(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @return MutableObjectLongMap<K>
	 */
	public static final <K> MutableObjectLongMap<K> newObjectLongHashMap() {
		return new ObjectLongHashMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectLongMap<K>
	 */
	public static final <K> MutableObjectLongMap<K> newObjectLongHashMap(int capacity) {
		return new ObjectLongHashMap<>(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectLongMap<K>
	 */
	public static final <K> MutableObjectLongMap<K> newObjectLongHashMap(Capacity capacity) {
		return new ObjectLongHashMap<>(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @return MutableObjectDoubleMap<K>
	 */
	public static final <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap() {
		return new ObjectDoubleHashMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectDoubleMap<K>
	 */
	public static final <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap(int capacity) {
		return new ObjectDoubleHashMap<>(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectDoubleMap<K>
	 */
	public static final <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap(Capacity capacity) {
		return new ObjectDoubleHashMap<>(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return MutableMap<K, V>
	 */
	public static final <K, V> MutableMap<K, V> newUnifiedMap() {
		return new UnifiedMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return MutableMap<K, V>
	 */
	public static final <K, V> MutableMap<K, V> newUnifiedMap(int capacity) {
		return new UnifiedMap<>(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return MutableMap<K, V>
	 */
	public static final <K, V> MutableMap<K, V> newUnifiedMap(Capacity capacity) {
		return new UnifiedMap<>(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return MutableMap<K, V>
	 */
	public static final <K, V> MutableMap<K, V> newUnifiedMap(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return new UnifiedMap<>();
		return new UnifiedMap<>(map);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param supplier
	 * @return MutableMap<K, V>
	 */
	public static final <K, V> MutableMap<K, V> newUnifiedMap(Supplier<Map<K, V>> supplier) {
		if (supplier == null)
			return new UnifiedMap<>();
		return newUnifiedMap(supplier.get());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param key
	 * @param value
	 * @return MutableMap<K, V>
	 */
	public static final <K, V> MutableMap<K, V> newUnifiedMap(K key, V value) {
		return UnifiedMap.newWithKeysValues(key, value);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param key0
	 * @param value0
	 * @param key1
	 * @param value1
	 * @return MutableMap<K, V>
	 */
	public static final <K, V> MutableMap<K, V> newUnifiedMap(K key0, V value0, K key1, V value1) {
		return UnifiedMap.newWithKeysValues(key0, value0, key1, value1);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param key0
	 * @param value0
	 * @param key1
	 * @param value1
	 * @param key2
	 * @param value2
	 * @return MutableMap<K, V>
	 */
	public static final <K, V> MutableMap<K, V> newUnifiedMap(K key0, V value0, K key1, V value1, K key2, V value2) {
		return UnifiedMap.newWithKeysValues(key0, value0, key1, value1, key2, value2);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param key0
	 * @param value0
	 * @param key1
	 * @param value1
	 * @param key2
	 * @param value2
	 * @param key3
	 * @param value3
	 * @return MutableMap<K, V>
	 */
	public static final <K, V> MutableMap<K, V> newUnifiedMap(K key0, V value0, K key1, V value1, K key2, V value2,
			K key3, V value3) {
		return UnifiedMap.newWithKeysValues(key0, value0, key1, value1, key2, value2, key3, value3);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static final <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap() {
		return ConcurrentHashMap.newMap();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static final <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(int capacity) {
		return ConcurrentHashMap.newMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static final <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Capacity capacity) {
		return ConcurrentHashMap.newMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static final <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return ConcurrentHashMap.newMap();
		return ConcurrentHashMap.newMap(map);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param supplier
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static final <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Supplier<Map<K, V>> supplier) {
		if (supplier == null)
			return ConcurrentHashMap.newMap();
		return newConcurrentHashMap(supplier.get());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static final <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe() {
		return ConcurrentHashMapUnsafe.newMap();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static final <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(int capacity) {
		return ConcurrentHashMapUnsafe.newMap(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static final <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Capacity capacity) {
		return ConcurrentHashMapUnsafe.newMap(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static final <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return ConcurrentHashMapUnsafe.newMap();
		return ConcurrentHashMapUnsafe.newMap(map);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param supplier
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static final <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Supplier<Map<K, V>> supplier) {
		if (supplier == null)
			return ConcurrentHashMapUnsafe.newMap();
		return newConcurrentHashMapUnsafe(supplier.get());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return MutableBiMap<K, V>
	 */
	public static final <K, V> MutableBiMap<K, V> newHashBiMap() {
		return new HashBiMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return MutableBiMap<K, V>
	 */
	public static final <K, V> MutableBiMap<K, V> newHashBiMap(int capacity) {
		return new HashBiMap<>(MapUtil.optimizationCapacity(capacity));
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return MutableBiMap<K, V>
	 */
	public static final <K, V> MutableBiMap<K, V> newHashBiMap(Capacity capacity) {
		return new HashBiMap<>(capacity == null ? Capacity.L04_SIZE.value() : capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return MutableSortedMap<K, V>
	 */
	public static final <K, V> MutableSortedMap<K, V> newTreeSortedMap() {
		return TreeSortedMap.newMap();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return MutableSortedMap<K, V>
	 */
	public static final <K, V> MutableSortedMap<K, V> newTreeSortedMap(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return TreeSortedMap.newMap();
		return TreeSortedMap.newMap(map);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param supplier
	 * @return MutableSortedMap<K, V>
	 */
	public static final <K, V> MutableSortedMap<K, V> newTreeSortedMap(Supplier<Map<K, V>> supplier) {
		if (supplier == null)
			return TreeSortedMap.newMap();
		return newTreeSortedMap(supplier.get());
	}

}
