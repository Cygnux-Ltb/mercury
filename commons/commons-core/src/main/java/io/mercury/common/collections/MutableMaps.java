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

public final class MutableMaps {

	private MutableMaps() {
	}

	/********** Key -> int **********/
	/**
	 * 
	 * @return MutableIntIntMap
	 */
	public static MutableIntIntMap newIntIntHashMap() {
		return new IntIntHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntIntMap
	 */
	public static MutableIntIntMap newIntIntHashMap(Capacity capacity) {
		return new IntIntHashMap(capacity.value());
	}

	/**
	 * 
	 * @return MutableIntLongMap
	 */
	public static MutableIntLongMap newIntLongHashMap() {
		return new IntLongHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntLongMap
	 */
	public static MutableIntLongMap newIntLongHashMap(Capacity capacity) {
		return new IntLongHashMap(capacity.value());
	}

	/**
	 * 
	 * @return MutableIntDoubleMap
	 */
	public static MutableIntDoubleMap newIntDoubleHashMap() {
		return new IntDoubleHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntDoubleMap
	 */
	public static MutableIntDoubleMap newIntDoubleHashMap(Capacity capacity) {
		return new IntDoubleHashMap(capacity.value());
	}

	/**
	 * 
	 * @return MutableIntBooleanMap
	 */
	public static MutableIntBooleanMap newIntBooleanHashMap() {
		return new IntBooleanHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntBooleanMap
	 */
	public static MutableIntBooleanMap newIntBooleanHashMap(Capacity capacity) {
		return new IntBooleanHashMap(capacity.value());
	}

	/**
	 * 
	 * @param <V>
	 * @return MutableIntObjectMap<V>
	 */
	public static <V> MutableIntObjectMap<V> newIntObjectHashMap() {
		return new IntObjectHashMap<>();
	}

	/**
	 * 
	 * @param <V>
	 * @param capacity
	 * @return MutableIntObjectMap<V>
	 */
	public static <V> MutableIntObjectMap<V> newIntObjectHashMap(Capacity capacity) {
		return new IntObjectHashMap<>(capacity.value());
	}

	/********** Key -> long **********/
	/**
	 * 
	 * @return MutableLongLongMap
	 */
	public static MutableLongLongMap newLongLongHashMap() {
		return new LongLongHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongLongMap
	 */
	public static MutableLongLongMap newLongLongHashMap(Capacity capacity) {
		return new LongLongHashMap(capacity.value());
	}

	/**
	 * 
	 * @return MutableLongIntMap
	 */
	public static MutableLongIntMap newLongIntHashMap() {
		return new LongIntHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongIntMap
	 */
	public static MutableLongIntMap newLongIntHashMap(Capacity capacity) {
		return new LongIntHashMap(capacity.value());
	}

	/**
	 * 
	 * @return MutableLongDoubleMap
	 */
	public static MutableLongDoubleMap newLongDoubleHashMap() {
		return new LongDoubleHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongDoubleMap
	 */
	public static MutableLongDoubleMap newLongDoubleHashMap(Capacity capacity) {
		return new LongDoubleHashMap(capacity.value());
	}

	/**
	 * 
	 * @return MutableLongBooleanMap
	 */
	public static MutableLongBooleanMap newLongBooleanHashMap() {
		return new LongBooleanHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongBooleanMap
	 */
	public static MutableLongBooleanMap newLongBooleanHashMap(Capacity capacity) {
		return new LongBooleanHashMap(capacity.value());
	}

	/**
	 * 
	 * @param <V>
	 * @return MutableLongObjectMap<V>
	 */
	public static <V> MutableLongObjectMap<V> newLongObjectHashMap() {
		return new LongObjectHashMap<>();
	}

	/**
	 * 
	 * @param <V>
	 * @param capacity
	 * @return MutableLongObjectMap<V>
	 */
	public static <V> MutableLongObjectMap<V> newLongObjectHashMap(Capacity capacity) {
		return new LongObjectHashMap<>(capacity.value());
	}

	/********** Key -> double **********/
	/**
	 * 
	 * @return MutableDoubleBooleanMap
	 */
	public static MutableDoubleBooleanMap newDoubleBooleanHashMap() {
		return new DoubleBooleanHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableDoubleBooleanMap
	 */
	public static MutableDoubleBooleanMap newDoubleBooleanHashMap(Capacity capacity) {
		return new DoubleBooleanHashMap(capacity.value());
	}

	/**
	 * 
	 * @return MutableDoubleIntMap
	 */
	public static MutableDoubleIntMap newDoubleIntHashMap() {
		return new DoubleIntHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableDoubleIntMap
	 */
	public static MutableDoubleIntMap newDoubleIntHashMap(Capacity capacity) {
		return new DoubleIntHashMap(capacity.value());
	}

	/**
	 * 
	 * @return MutableDoubleLongMap
	 */
	public static MutableDoubleLongMap newDoubleLongHashMap() {
		return new DoubleLongHashMap();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableDoubleLongMap
	 */
	public static MutableDoubleLongMap newDoubleLongHashMap(Capacity capacity) {
		return new DoubleLongHashMap(capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @return MutableObjectBooleanMap<K>
	 */
	public static <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap() {
		return new ObjectBooleanHashMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectBooleanMap<K>
	 */
	public static <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap(Capacity capacity) {
		return new ObjectBooleanHashMap<>(capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @return MutableObjectIntMap<K>
	 */
	public static <K> MutableObjectIntMap<K> newObjectIntHashMap() {
		return new ObjectIntHashMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectIntMap<K>
	 */
	public static <K> MutableObjectIntMap<K> newObjectIntHashMap(Capacity capacity) {
		return new ObjectIntHashMap<>(capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @return MutableObjectLongMap<K>
	 */
	public static <K> MutableObjectLongMap<K> newObjectLongHashMap() {
		return new ObjectLongHashMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectLongMap<K>
	 */
	public static <K> MutableObjectLongMap<K> newObjectLongHashMap(Capacity capacity) {
		return new ObjectLongHashMap<>(capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @return MutableObjectDoubleMap<K>
	 */
	public static <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap() {
		return new ObjectDoubleHashMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param capacity
	 * @return MutableObjectDoubleMap<K>
	 */
	public static <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap(Capacity capacity) {
		return new ObjectDoubleHashMap<>(capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return MutableMap<K, V>
	 */
	public static <K, V> MutableMap<K, V> newUnifiedMap() {
		return UnifiedMap.newMap();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return MutableMap<K, V>
	 */
	public static <K, V> MutableMap<K, V> newUnifiedMap(Capacity capacity) {
		return UnifiedMap.newMap(capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return MutableMap<K, V>
	 */
	public static <K, V> MutableMap<K, V> newUnifiedMap(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return newUnifiedMap();
		return UnifiedMap.newMap(map);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param supplier
	 * @return MutableMap<K, V>
	 */
	public static <K, V> MutableMap<K, V> newUnifiedMap(Supplier<Map<K, V>> supplier) {
		if (supplier == null)
			return newUnifiedMap();
		return newUnifiedMap(supplier.get());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap() {
		return ConcurrentHashMap.newMap();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Capacity capacity) {
		return ConcurrentHashMap.newMap(capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return newConcurrentHashMap();
		return ConcurrentHashMap.newMap(map);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe() {
		return ConcurrentHashMapUnsafe.newMap();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Capacity capacity) {
		return ConcurrentHashMapUnsafe.newMap(capacity.value());
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return ConcurrentMutableMap<K, V>
	 */
	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return newConcurrentHashMapUnsafe();
		return ConcurrentHashMapUnsafe.newMap(map);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return MutableBiMap<K, V>
	 */
	public static <K, V> MutableBiMap<K, V> newHashBiMap() {
		return new HashBiMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param capacity
	 * @return MutableBiMap<K, V>
	 */
	public static <K, V> MutableBiMap<K, V> newHashBiMap(Capacity capacity) {
		return new HashBiMap<>(capacity.value());
	}

}
