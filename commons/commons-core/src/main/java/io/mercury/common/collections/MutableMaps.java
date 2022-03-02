package io.mercury.common.collections;

import static io.mercury.common.collections.Capacity.checkAndGet;
import static io.mercury.common.collections.MapUtil.optimizationCapacity;

import java.util.Arrays;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import io.mercury.common.lang.Assertor;
import io.mercury.common.util.ArrayUtil;
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

import javax.annotation.Nonnull;

public final class MutableMaps {

    private MutableMaps() {
    }

    /* ********* Key -> int **********/
    /**
     * @return MutableIntIntMap
     */
    public static MutableIntIntMap newIntIntHashMap() {
        return new IntIntHashMap();
    }

    /**
     * @param capacity
     * @return MutableIntIntMap
     */
    public static MutableIntIntMap newIntIntHashMap(int capacity) {
        return new IntIntHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param capacity
     * @return MutableIntIntMap
     */
    public static MutableIntIntMap newIntIntHashMap(Capacity capacity) {
        return new IntIntHashMap(checkAndGet(capacity));
    }

    /**
     * @return MutableIntLongMap
     */
    public static MutableIntLongMap newIntLongHashMap() {
        return new IntLongHashMap();
    }

    /**
     * @param capacity
     * @return MutableIntLongMap
     */
    public static MutableIntLongMap newIntLongHashMap(int capacity) {
        return new IntLongHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param capacity
     * @return MutableIntLongMap
     */
    public static MutableIntLongMap newIntLongHashMap(Capacity capacity) {
        return new IntLongHashMap(checkAndGet(capacity));
    }

    /**
     * @return MutableIntDoubleMap
     */
    public static MutableIntDoubleMap newIntDoubleHashMap() {
        return new IntDoubleHashMap();
    }

    /**
     * @param capacity
     * @return MutableIntDoubleMap
     */
    public static MutableIntDoubleMap newIntDoubleHashMap(int capacity) {
        return new IntDoubleHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param capacity
     * @return MutableIntDoubleMap
     */
    public static MutableIntDoubleMap newIntDoubleHashMap(Capacity capacity) {
        return new IntDoubleHashMap(checkAndGet(capacity));
    }

    /**
     * @return MutableIntBooleanMap
     */
    public static MutableIntBooleanMap newIntBooleanHashMap() {
        return new IntBooleanHashMap();
    }

    /**
     * @param capacity
     * @return MutableIntBooleanMap
     */
    public static MutableIntBooleanMap newIntBooleanHashMap(int capacity) {
        return new IntBooleanHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param capacity
     * @return MutableIntBooleanMap
     */
    public static MutableIntBooleanMap newIntBooleanHashMap(Capacity capacity) {
        return new IntBooleanHashMap(checkAndGet(capacity));
    }

    /**
     * @param <V>
     * @return MutableIntObjectMap<V>
     */
    public static <V> MutableIntObjectMap<V> newIntObjectHashMap() {
        return new IntObjectHashMap<>();
    }

    /**
     * @param <V>
     * @param capacity
     * @return MutableIntObjectMap<V>
     */
    public static <V> MutableIntObjectMap<V> newIntObjectHashMap(int capacity) {
        return new IntObjectHashMap<>(optimizationCapacity(capacity));
    }

    /**
     * @param <V>
     * @param capacity
     * @return MutableIntObjectMap<V>
     */
    public static <V> MutableIntObjectMap<V> newIntObjectHashMap(Capacity capacity) {
        return new IntObjectHashMap<>(checkAndGet(capacity));
    }

    /**
     * @param keyFunc
     * @param values
     * @param <V>
     * @return MutableIntObjectMap<V>
     */
    public static <V> MutableIntObjectMap<V> newIntObjectHashMap(@Nonnull ToIntFunction<V> keyFunc, V... values) {
        Assertor.nonNull(keyFunc, "keyFunc");
        if (ArrayUtil.isNullOrEmpty(values)) {
            return newIntObjectHashMap();
        }
        MutableIntObjectMap<V> map = newIntObjectHashMap(values.length);
        for (V value : values) {
            map.put(keyFunc.applyAsInt(value), value);
        }
        return map;
    }

    /* ********* Key -> long **********/
    /**
     * @return MutableLongLongMap
     */
    public static MutableLongLongMap newLongLongHashMap() {
        return new LongLongHashMap();
    }

    /**
     * @param capacity
     * @return MutableLongLongMap
     */
    public static MutableLongLongMap newLongLongHashMap(int capacity) {
        return new LongLongHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param capacity
     * @return MutableLongLongMap
     */
    public static MutableLongLongMap newLongLongHashMap(Capacity capacity) {
        return new LongLongHashMap(checkAndGet(capacity));
    }

    /**
     * @return MutableLongIntMap
     */
    public static MutableLongIntMap newLongIntHashMap() {
        return new LongIntHashMap();
    }

    /**
     * @param capacity
     * @return MutableLongIntMap
     */
    public static MutableLongIntMap newLongIntHashMap(int capacity) {
        return new LongIntHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param capacity
     * @return MutableLongIntMap
     */
    public static MutableLongIntMap newLongIntHashMap(Capacity capacity) {
        return new LongIntHashMap(checkAndGet(capacity));
    }

    /**
     * @return MutableLongDoubleMap
     */
    public static MutableLongDoubleMap newLongDoubleHashMap() {
        return new LongDoubleHashMap();
    }

    /**
     * @param capacity
     * @return MutableLongDoubleMap
     */
    public static MutableLongDoubleMap newLongDoubleHashMap(int capacity) {
        return new LongDoubleHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param capacity
     * @return MutableLongDoubleMap
     */
    public static MutableLongDoubleMap newLongDoubleHashMap(Capacity capacity) {
        return new LongDoubleHashMap(checkAndGet(capacity));
    }

    /**
     * @return MutableLongBooleanMap
     */
    public static MutableLongBooleanMap newLongBooleanHashMap() {
        return new LongBooleanHashMap();
    }

    /**
     * @param capacity
     * @return MutableLongBooleanMap
     */
    public static MutableLongBooleanMap newLongBooleanHashMap(int capacity) {
        return new LongBooleanHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param capacity
     * @return MutableLongBooleanMap
     */
    public static MutableLongBooleanMap newLongBooleanHashMap(Capacity capacity) {
        return new LongBooleanHashMap(checkAndGet(capacity));
    }

    /**
     * @param <V>
     * @return MutableLongObjectMap<V>
     */
    public static <V> MutableLongObjectMap<V> newLongObjectHashMap() {
        return new LongObjectHashMap<>();
    }

    /**
     * @param <V>
     * @param capacity
     * @return MutableLongObjectMap<V>
     */
    public static <V> MutableLongObjectMap<V> newLongObjectHashMap(int capacity) {
        return new LongObjectHashMap<>(optimizationCapacity(capacity));
    }

    /**
     * @param <V>
     * @param capacity
     * @return MutableLongObjectMap<V>
     */
    public static <V> MutableLongObjectMap<V> newLongObjectHashMap(Capacity capacity) {
        return new LongObjectHashMap<>(checkAndGet(capacity));
    }

    /* ********* Key -> double **********/
    /**
     * @return MutableDoubleBooleanMap
     */
    public static MutableDoubleBooleanMap newDoubleBooleanHashMap() {
        return new DoubleBooleanHashMap();
    }

    /**
     * @param capacity
     * @return MutableDoubleBooleanMap
     */
    public static MutableDoubleBooleanMap newDoubleBooleanHashMap(int capacity) {
        return new DoubleBooleanHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param capacity
     * @return MutableDoubleBooleanMap
     */
    public static MutableDoubleBooleanMap newDoubleBooleanHashMap(Capacity capacity) {
        return new DoubleBooleanHashMap(checkAndGet(capacity));
    }

    /**
     * @return MutableDoubleIntMap
     */
    public static MutableDoubleIntMap newDoubleIntHashMap() {
        return new DoubleIntHashMap();
    }

    /**
     * @param capacity
     * @return MutableDoubleIntMap
     */
    public static MutableDoubleIntMap newDoubleIntHashMap(int capacity) {
        return new DoubleIntHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param capacity
     * @return MutableDoubleIntMap
     */
    public static MutableDoubleIntMap newDoubleIntHashMap(Capacity capacity) {
        return new DoubleIntHashMap(checkAndGet(capacity));
    }

    /**
     * @return MutableDoubleLongMap
     */
    public static MutableDoubleLongMap newDoubleLongHashMap() {
        return new DoubleLongHashMap();
    }

    /**
     * @param capacity
     * @return MutableDoubleLongMap
     */
    public static MutableDoubleLongMap newDoubleLongHashMap(int capacity) {
        return new DoubleLongHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param capacity
     * @return MutableDoubleLongMap
     */
    public static MutableDoubleLongMap newDoubleLongHashMap(Capacity capacity) {
        return new DoubleLongHashMap(checkAndGet(capacity));
    }

    /**
     * @param <K>
     * @return MutableObjectBooleanMap<K>
     */
    public static <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap() {
        return new ObjectBooleanHashMap<>();
    }

    /**
     * @param <K>
     * @param capacity
     * @return MutableObjectBooleanMap<K>
     */
    public static <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap(int capacity) {
        return new ObjectBooleanHashMap<>(optimizationCapacity(capacity));
    }

    /**
     * @param <K>
     * @param capacity
     * @return MutableObjectBooleanMap<K>
     */
    public static <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap(Capacity capacity) {
        return new ObjectBooleanHashMap<>(checkAndGet(capacity));
    }

    /**
     * @param <K>
     * @return MutableObjectIntMap<K>
     */
    public static <K> MutableObjectIntMap<K> newObjectIntHashMap() {
        return new ObjectIntHashMap<>();
    }

    /**
     * @param <K>
     * @param capacity
     * @return MutableObjectIntMap<K>
     */
    public static <K> MutableObjectIntMap<K> newObjectIntHashMap(int capacity) {
        return new ObjectIntHashMap<>(optimizationCapacity(capacity));
    }

    /**
     * @param <K>
     * @param capacity
     * @return MutableObjectIntMap<K>
     */
    public static <K> MutableObjectIntMap<K> newObjectIntHashMap(Capacity capacity) {
        return new ObjectIntHashMap<>(checkAndGet(capacity));
    }

    /**
     * @param <K>
     * @return MutableObjectLongMap<K>
     */
    public static <K> MutableObjectLongMap<K> newObjectLongHashMap() {
        return new ObjectLongHashMap<>();
    }

    /**
     * @param <K>
     * @param capacity
     * @return MutableObjectLongMap<K>
     */
    public static <K> MutableObjectLongMap<K> newObjectLongHashMap(int capacity) {
        return new ObjectLongHashMap<>(optimizationCapacity(capacity));
    }

    /**
     * @param <K>
     * @param capacity
     * @return MutableObjectLongMap<K>
     */
    public static <K> MutableObjectLongMap<K> newObjectLongHashMap(Capacity capacity) {
        return new ObjectLongHashMap<>(checkAndGet(capacity));
    }

    /**
     * @param <K>
     * @return MutableObjectDoubleMap<K>
     */
    public static <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap() {
        return new ObjectDoubleHashMap<>();
    }

    /**
     * @param <K>
     * @param capacity
     * @return MutableObjectDoubleMap<K>
     */
    public static <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap(int capacity) {
        return new ObjectDoubleHashMap<>(optimizationCapacity(capacity));
    }

    /**
     * @param <K>
     * @param capacity
     * @return MutableObjectDoubleMap<K>
     */
    public static <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap(Capacity capacity) {
        return new ObjectDoubleHashMap<>(checkAndGet(capacity));
    }

    /**
     * @param <K>
     * @param <V>
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap() {
        return new UnifiedMap<>();
    }

    /**
     * @param <K>
     * @param <V>
     * @param capacity
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(int capacity) {
        return new UnifiedMap<>(optimizationCapacity(capacity));
    }

    /**
     * @param <K>
     * @param <V>
     * @param capacity
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(Capacity capacity) {
        return new UnifiedMap<>(checkAndGet(capacity));
    }

    /**
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(Map<K, V> map) {
        if (map == null || map.isEmpty())
            return new UnifiedMap<>();
        return new UnifiedMap<>(map);
    }

    /**
     * @param <K>
     * @param <V>
     * @param supplier
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(Supplier<Map<K, V>> supplier) {
        if (supplier == null)
            return new UnifiedMap<>();
        return newUnifiedMap(supplier.get());
    }

    /**
     * @param <K>
     * @param <V>
     * @param key
     * @param value
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(K key, V value) {
        return UnifiedMap.newWithKeysValues(key, value);
    }

    /**
     * @param <K>
     * @param <V>
     * @param key0
     * @param value0
     * @param key1
     * @param value1
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(K key0, V value0, K key1, V value1) {
        return UnifiedMap.newWithKeysValues(key0, value0, key1, value1);
    }

    /**
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
    public static <K, V> MutableMap<K, V> newUnifiedMap(K key0, V value0, K key1, V value1, K key2, V value2) {
        return UnifiedMap.newWithKeysValues(key0, value0, key1, value1, key2, value2);
    }

    /**
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
    public static <K, V> MutableMap<K, V> newUnifiedMap(K key0, V value0, K key1, V value1, K key2, V value2,
                                                        K key3, V value3) {
        return UnifiedMap.newWithKeysValues(key0, value0, key1, value1, key2, value2, key3, value3);
    }

    /**
     * @param <K>
     * @param <V>
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap() {
        return ConcurrentHashMap.newMap();
    }

    /**
     * @param <K>
     * @param <V>
     * @param capacity
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(int capacity) {
        return ConcurrentHashMap.newMap(optimizationCapacity(capacity));
    }

    /**
     * @param <K>
     * @param <V>
     * @param capacity
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Capacity capacity) {
        return ConcurrentHashMap.newMap(checkAndGet(capacity));
    }

    /**
     * @param <K>
     * @param <V>
     * @param map
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Map<K, V> map) {
        if (map == null || map.isEmpty())
            return ConcurrentHashMap.newMap();
        return ConcurrentHashMap.newMap(map);
    }

    /**
     * @param <K>
     * @param <V>
     * @param supplier
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Supplier<Map<K, V>> supplier) {
        if (supplier == null)
            return ConcurrentHashMap.newMap();
        return newConcurrentHashMap(supplier.get());
    }

    /**
     * @param <K>
     * @param <V>
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe() {
        return ConcurrentHashMapUnsafe.newMap();
    }

    /**
     * @param <K>
     * @param <V>
     * @param capacity
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(int capacity) {
        return ConcurrentHashMapUnsafe.newMap(optimizationCapacity(capacity));
    }

    /**
     * @param <K>
     * @param <V>
     * @param capacity
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Capacity capacity) {
        return ConcurrentHashMapUnsafe.newMap(checkAndGet(capacity));
    }

    /**
     * @param <K>
     * @param <V>
     * @param map
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Map<K, V> map) {
        if (map == null || map.isEmpty())
            return ConcurrentHashMapUnsafe.newMap();
        return ConcurrentHashMapUnsafe.newMap(map);
    }

    /**
     * @param <K>
     * @param <V>
     * @param supplier
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Supplier<Map<K, V>> supplier) {
        if (supplier == null)
            return ConcurrentHashMapUnsafe.newMap();
        return newConcurrentHashMapUnsafe(supplier.get());
    }

    /**
     * @param <K>
     * @param <V>
     * @return MutableBiMap<K, V>
     */
    public static <K, V> MutableBiMap<K, V> newHashBiMap() {
        return new HashBiMap<>();
    }

    /**
     * @param <K>
     * @param <V>
     * @param capacity
     * @return MutableBiMap<K, V>
     */
    public static <K, V> MutableBiMap<K, V> newHashBiMap(int capacity) {
        return new HashBiMap<>(optimizationCapacity(capacity));
    }

    /**
     * @param <K>
     * @param <V>
     * @param capacity
     * @return MutableBiMap<K, V>
     */
    public static <K, V> MutableBiMap<K, V> newHashBiMap(Capacity capacity) {
        return new HashBiMap<>(checkAndGet(capacity));
    }

    /**
     * @param <K>
     * @param <V>
     * @return MutableSortedMap<K, V>
     */
    public static <K, V> MutableSortedMap<K, V> newTreeSortedMap() {
        return TreeSortedMap.newMap();
    }

    /**
     * @param <K>
     * @param <V>
     * @param map
     * @return MutableSortedMap<K, V>
     */
    public static <K, V> MutableSortedMap<K, V> newTreeSortedMap(Map<K, V> map) {
        if (map == null || map.isEmpty())
            return TreeSortedMap.newMap();
        return TreeSortedMap.newMap(map);
    }

    /**
     * @param <K>
     * @param <V>
     * @param supplier
     * @return MutableSortedMap<K, V>
     */
    public static <K, V> MutableSortedMap<K, V> newTreeSortedMap(Supplier<Map<K, V>> supplier) {
        if (supplier == null)
            return TreeSortedMap.newMap();
        return newTreeSortedMap(supplier.get());
    }

}
