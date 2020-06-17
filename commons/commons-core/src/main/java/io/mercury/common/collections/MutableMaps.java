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

	/**
	 * primitive map
	 */

	// Key -> int
	public static MutableIntIntMap newIntIntHashMap() {
		return new IntIntHashMap();
	}

	public static MutableIntIntMap newIntIntHashMap(Capacity capacity) {
		return new IntIntHashMap(capacity.size());
	}

	public static MutableIntLongMap newIntLongHashMap() {
		return new IntLongHashMap();
	}

	public static MutableIntLongMap newIntLongHashMap(Capacity capacity) {
		return new IntLongHashMap(capacity.size());
	}

	public static MutableIntDoubleMap newIntDoubleHashMap() {
		return new IntDoubleHashMap();
	}

	public static MutableIntDoubleMap newIntDoubleHashMap(Capacity capacity) {
		return new IntDoubleHashMap(capacity.size());
	}

	public static MutableIntBooleanMap newIntBooleanHashMap() {
		return new IntBooleanHashMap();
	}

	public static MutableIntBooleanMap newIntBooleanHashMap(Capacity capacity) {
		return new IntBooleanHashMap(capacity.size());
	}

	public static <V> MutableIntObjectMap<V> newIntObjectHashMap() {
		return new IntObjectHashMap<>();
	}

	public static <V> MutableIntObjectMap<V> newIntObjectHashMap(Capacity capacity) {
		return new IntObjectHashMap<>(capacity.size());
	}

	// Key -> long
	public static MutableLongLongMap newLongLongHashMap() {
		return new LongLongHashMap();
	}

	public static MutableLongLongMap newLongLongHashMap(Capacity capacity) {
		return new LongLongHashMap(capacity.size());
	}

	public static MutableLongIntMap newLongIntHashMap() {
		return new LongIntHashMap();
	}

	public static MutableLongIntMap newLongIntHashMap(Capacity capacity) {
		return new LongIntHashMap(capacity.size());
	}

	public static MutableLongDoubleMap newLongDoubleHashMap() {
		return new LongDoubleHashMap();
	}

	public static MutableLongDoubleMap newLongDoubleHashMap(Capacity capacity) {
		return new LongDoubleHashMap(capacity.size());
	}

	public static MutableLongBooleanMap newLongBooleanHashMap() {
		return new LongBooleanHashMap();
	}

	public static MutableLongBooleanMap newLongBooleanHashMap(Capacity capacity) {
		return new LongBooleanHashMap(capacity.size());
	}

	public static <V> MutableLongObjectMap<V> newLongObjectHashMap() {
		return new LongObjectHashMap<>();
	}

	public static <V> MutableLongObjectMap<V> newLongObjectHashMap(Capacity capacity) {
		return new LongObjectHashMap<>(capacity.size());
	}

	// Key -> double
	public static MutableDoubleBooleanMap newDoubleBooleanHashMap() {
		return new DoubleBooleanHashMap();
	}

	public static MutableDoubleBooleanMap newDoubleBooleanHashMap(Capacity capacity) {
		return new DoubleBooleanHashMap(capacity.size());
	}

	public static MutableDoubleIntMap newDoubleIntHashMap() {
		return new DoubleIntHashMap();
	}

	public static MutableDoubleIntMap newDoubleIntHashMap(Capacity capacity) {
		return new DoubleIntHashMap(capacity.size());
	}

	public static MutableDoubleLongMap newDoubleLongHashMap() {
		return new DoubleLongHashMap();
	}

	public static MutableDoubleLongMap newDoubleLongHashMap(Capacity capacity) {
		return new DoubleLongHashMap(capacity.size());
	}

	// Key -> Object
	public static <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap() {
		return new ObjectBooleanHashMap<>();
	}

	public static <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap(Capacity capacity) {
		return new ObjectBooleanHashMap<>(capacity.size());
	}

	public static <K> MutableObjectIntMap<K> newObjectIntHashMap() {
		return new ObjectIntHashMap<>();
	}

	public static <K> MutableObjectIntMap<K> newObjectIntHashMap(Capacity capacity) {
		return new ObjectIntHashMap<>(capacity.size());
	}

	public static <K> MutableObjectLongMap<K> newObjectLongHashMap() {
		return new ObjectLongHashMap<>();
	}

	public static <K> MutableObjectLongMap<K> newObjectLongHashMap(Capacity capacity) {
		return new ObjectLongHashMap<>(capacity.size());
	}

	public static <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap() {
		return new ObjectDoubleHashMap<>();
	}

	public static <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap(Capacity capacity) {
		return new ObjectDoubleHashMap<>(capacity.size());
	}

	/**
	 * map
	 */
	public static <K, V> MutableMap<K, V> newUnifiedMap() {
		return UnifiedMap.newMap();
	}

	public static <K, V> MutableMap<K, V> newUnifiedMap(Capacity capacity) {
		return UnifiedMap.newMap(capacity.size());
	}

	public static <K, V> MutableMap<K, V> newUnifiedMap(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return newUnifiedMap();
		return UnifiedMap.newMap(map);
	}

	public static <K, V> MutableMap<K, V> newUnifiedMap(Supplier<Map<K, V>> supplier) {
		if (supplier == null)
			return newUnifiedMap();
		return newUnifiedMap(supplier.get());
	}

	/**
	 * concurrent map
	 */
	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap() {
		return ConcurrentHashMap.newMap();
	}

	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Capacity capacity) {
		return ConcurrentHashMap.newMap(capacity.size());
	}

	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return newConcurrentHashMap();
		return ConcurrentHashMap.newMap(map);
	}

	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe() {
		return ConcurrentHashMapUnsafe.newMap();
	}

	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Capacity capacity) {
		return ConcurrentHashMapUnsafe.newMap(capacity.size());
	}

	public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return newConcurrentHashMapUnsafe();
		return ConcurrentHashMapUnsafe.newMap(map);
	}

	public static <K, V> MutableBiMap<K, V> newHashBiMap() {
		return new HashBiMap<>();
	}

	public static <K, V> MutableBiMap<K, V> newHashBiMap(Capacity capacity) {
		return new HashBiMap<>(capacity.size());
	}

}
