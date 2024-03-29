package io.mercury.common.collections;

import io.mercury.common.lang.Asserter;
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
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static io.mercury.common.collections.MapUtil.optimizationCapacity;

public final class MutableMaps {

    private MutableMaps() {
    }

    /*
     * ******************** key -> int ********************
     */

    /**
     * @return MutableIntIntMap
     */
    public static MutableIntIntMap newIntIntHashMap() {
        return new IntIntHashMap();
    }

    /**
     * @param capacity int
     * @return MutableIntIntMap
     */
    public static MutableIntIntMap newIntIntHashMap(int capacity) {
        return new IntIntHashMap(optimizationCapacity(capacity));
    }


    /**
     * @return MutableIntLongMap
     */
    public static MutableIntLongMap newIntLongHashMap() {
        return new IntLongHashMap();
    }

    /**
     * @param capacity int
     * @return MutableIntLongMap
     */
    public static MutableIntLongMap newIntLongHashMap(int capacity) {
        return new IntLongHashMap(optimizationCapacity(capacity));
    }


    /**
     * @return MutableIntDoubleMap
     */
    public static MutableIntDoubleMap newIntDoubleHashMap() {
        return new IntDoubleHashMap();
    }

    /**
     * @param capacity int
     * @return MutableIntDoubleMap
     */
    public static MutableIntDoubleMap newIntDoubleHashMap(int capacity) {
        return new IntDoubleHashMap(optimizationCapacity(capacity));
    }


    /**
     * @return MutableIntBooleanMap
     */
    public static MutableIntBooleanMap newIntBooleanHashMap() {
        return new IntBooleanHashMap();
    }

    /**
     * @param capacity int
     * @return MutableIntBooleanMap
     */
    public static MutableIntBooleanMap newIntBooleanHashMap(int capacity) {
        return new IntBooleanHashMap(optimizationCapacity(capacity));
    }


    /**
     * @param <V> Value type
     * @return MutableIntObjectMap<V>
     */
    public static <V> MutableIntObjectMap<V> newIntObjectHashMap() {
        return new IntObjectHashMap<>();
    }

    /**
     * @param <V>      Value type
     * @param capacity int
     * @return MutableIntObjectMap<V>
     */
    public static <V> MutableIntObjectMap<V> newIntObjectHashMap(int capacity) {
        return new IntObjectHashMap<>(optimizationCapacity(capacity));
    }

    /**
     * @param keyFunc ToIntFunction<V>
     * @param values  V...
     * @param <V>     Value type
     * @return MutableIntObjectMap<V>
     */
    @SafeVarargs
    public static <V> MutableIntObjectMap<V> newIntObjectHashMap(@Nonnull ToIntFunction<V> keyFunc, V... values) {
        Asserter.nonNull(keyFunc, "keyFunc");
        if (ArrayUtil.isNullOrEmpty(values))
            return newIntObjectHashMap();
        MutableIntObjectMap<V> map = newIntObjectHashMap(values.length * 2);
        for (V value : values) {
            if (value != null) map.put(keyFunc.applyAsInt(value), value);
        }
        return map;
    }

    /*
     * ******************** key -> long ********************
     */

    /**
     * @return MutableLongLongMap
     */
    public static MutableLongLongMap newLongLongHashMap() {
        return new LongLongHashMap();
    }

    /**
     * @param capacity int
     * @return MutableLongLongMap
     */
    public static MutableLongLongMap newLongLongHashMap(int capacity) {
        return new LongLongHashMap(optimizationCapacity(capacity));
    }

    /**
     * @return MutableLongIntMap
     */
    public static MutableLongIntMap newLongIntHashMap() {
        return new LongIntHashMap();
    }

    /**
     * @param capacity int
     * @return MutableLongIntMap
     */
    public static MutableLongIntMap newLongIntHashMap(int capacity) {
        return new LongIntHashMap(optimizationCapacity(capacity));
    }

    /**
     * @return MutableLongDoubleMap
     */
    public static MutableLongDoubleMap newLongDoubleHashMap() {
        return new LongDoubleHashMap();
    }

    /**
     * @param capacity int
     * @return MutableLongDoubleMap
     */
    public static MutableLongDoubleMap newLongDoubleHashMap(int capacity) {
        return new LongDoubleHashMap(optimizationCapacity(capacity));
    }

    /**
     * @return MutableLongBooleanMap
     */
    public static MutableLongBooleanMap newLongBooleanHashMap() {
        return new LongBooleanHashMap();
    }

    /**
     * @param capacity int
     * @return MutableLongBooleanMap
     */
    public static MutableLongBooleanMap newLongBooleanHashMap(int capacity) {
        return new LongBooleanHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param <V> Value type
     * @return MutableLongObjectMap<V>
     */
    public static <V> MutableLongObjectMap<V> newLongObjectHashMap() {
        return new LongObjectHashMap<>();
    }

    /**
     * @param <V>      Value type
     * @param capacity int
     * @return MutableLongObjectMap<V>
     */
    public static <V> MutableLongObjectMap<V> newLongObjectHashMap(int capacity) {
        return new LongObjectHashMap<>(optimizationCapacity(capacity));
    }

    /*
     * ******************** key -> double ********************
     */

    /**
     * @return MutableDoubleBooleanMap
     */
    public static MutableDoubleBooleanMap newDoubleBooleanHashMap() {
        return new DoubleBooleanHashMap();
    }

    /**
     * @param capacity int
     * @return MutableDoubleBooleanMap
     */
    public static MutableDoubleBooleanMap newDoubleBooleanHashMap(int capacity) {
        return new DoubleBooleanHashMap(optimizationCapacity(capacity));
    }


    /**
     * @return MutableDoubleIntMap
     */
    public static MutableDoubleIntMap newDoubleIntHashMap() {
        return new DoubleIntHashMap();
    }

    /**
     * @param capacity int
     * @return MutableDoubleIntMap
     */
    public static MutableDoubleIntMap newDoubleIntHashMap(int capacity) {
        return new DoubleIntHashMap(optimizationCapacity(capacity));
    }

    /*
     * ******************** key -> object ********************
     */

    /**
     * @return MutableDoubleLongMap
     */
    public static MutableDoubleLongMap newDoubleLongHashMap() {
        return new DoubleLongHashMap();
    }

    /**
     * @param capacity int
     * @return MutableDoubleLongMap
     */
    public static MutableDoubleLongMap newDoubleLongHashMap(int capacity) {
        return new DoubleLongHashMap(optimizationCapacity(capacity));
    }

    /**
     * @param <K> Key type
     * @return MutableObjectBooleanMap<K>
     */
    public static <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap() {
        return new ObjectBooleanHashMap<>();
    }

    /**
     * @param <K>      Key type
     * @param capacity int
     * @return MutableObjectBooleanMap<K>
     */
    public static <K> MutableObjectBooleanMap<K> newObjectBooleanHashMap(int capacity) {
        return new ObjectBooleanHashMap<>(optimizationCapacity(capacity));
    }


    /**
     * @param <K> Key type
     * @return MutableObjectIntMap<K>
     */
    public static <K> MutableObjectIntMap<K> newObjectIntHashMap() {
        return new ObjectIntHashMap<>();
    }

    /**
     * @param <K>      Key type
     * @param capacity int
     * @return MutableObjectIntMap<K>
     */
    public static <K> MutableObjectIntMap<K> newObjectIntHashMap(int capacity) {
        return new ObjectIntHashMap<>(optimizationCapacity(capacity));
    }


    /**
     * @param <K> Key type
     * @return MutableObjectLongMap<K>
     */
    public static <K> MutableObjectLongMap<K> newObjectLongHashMap() {
        return new ObjectLongHashMap<>();
    }

    /**
     * @param <K>      Key type
     * @param capacity int
     * @return MutableObjectLongMap<K>
     */
    public static <K> MutableObjectLongMap<K> newObjectLongHashMap(int capacity) {
        return new ObjectLongHashMap<>(optimizationCapacity(capacity));
    }


    /**
     * @param <K> Key type
     * @return MutableObjectDoubleMap<K>
     */
    public static <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap() {
        return new ObjectDoubleHashMap<>();
    }

    /**
     * @param <K>      Key type
     * @param capacity int
     * @return MutableObjectDoubleMap<K>
     */
    public static <K> MutableObjectDoubleMap<K> newObjectDoubleHashMap(int capacity) {
        return new ObjectDoubleHashMap<>(optimizationCapacity(capacity));
    }


    /**
     * @param <K> Key type
     * @param <V> Value type
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap() {
        return new UnifiedMap<>();
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param capacity int
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(int capacity) {
        return new UnifiedMap<>(optimizationCapacity(capacity));
    }


    /**
     * @param map Map<K, V>
     * @param <K> Key type
     * @param <V> Value type
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(Map<K, V> map) {
        if (map == null || map.isEmpty()) return new UnifiedMap<>();
        return new UnifiedMap<>(map);
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param supplier Supplier<Map<K, V>>
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(Supplier<Map<K, V>> supplier) {
        if (supplier == null) return new UnifiedMap<>();
        return newUnifiedMap(supplier.get());
    }

    /**
     * @param <K>   Key type
     * @param <V>   Value type
     * @param key   key
     * @param value value
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(K key, V value) {
        return UnifiedMap.newWithKeysValues(key, value);
    }

    /**
     * @param <K>    Key type
     * @param <V>    Value type
     * @param key0   key
     * @param value0 value
     * @param key1   key
     * @param value1 value
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(K key0, V value0,
                                                        K key1, V value1) {
        return UnifiedMap.newWithKeysValues(key0, value0, key1, value1);
    }

    /**
     * @param <K>    Key type
     * @param <V>    Value type
     * @param key0   key
     * @param value0 value
     * @param key1   key
     * @param value1 value
     * @param key2   key
     * @param value2 value
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(K key0, V value0,
                                                        K key1, V value1,
                                                        K key2, V value2) {
        return UnifiedMap.newWithKeysValues(
                key0, value0, key1, value1, key2, value2);
    }

    /**
     * @param <K>    Key type
     * @param <V>    Value type
     * @param key0   key
     * @param value0 value
     * @param key1   key
     * @param value1 value
     * @param key2   key
     * @param value2 value
     * @param key3   key
     * @param value3 value
     * @return MutableMap<K, V>
     */
    public static <K, V> MutableMap<K, V> newUnifiedMap(K key0, V value0,
                                                        K key1, V value1,
                                                        K key2, V value2,
                                                        K key3, V value3) {
        return UnifiedMap.newWithKeysValues(
                key0, value0, key1, value1, key2, value2, key3, value3);
    }

    /**
     * @param <K> Key type
     * @param <V> Value type
     * @return MutableBiMap<K, V>
     */
    public static <K, V> MutableBiMap<K, V> newHashBiMap() {
        return new HashBiMap<>();
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param capacity int
     * @return MutableBiMap<K, V>
     */
    public static <K, V> MutableBiMap<K, V> newHashBiMap(int capacity) {
        return new HashBiMap<>(optimizationCapacity(capacity));
    }


    /**
     * @param <K> Key type
     * @param <V> Value type
     * @return MutableSortedMap<K, V>
     */
    public static <K, V> MutableSortedMap<K, V> newTreeSortedMap() {
        return TreeSortedMap.newMap();
    }

    /**
     * @param <K> Key type
     * @param <V> Value type
     * @param map Map<K, V>
     * @return MutableSortedMap<K, V>
     */
    public static <K, V> MutableSortedMap<K, V> newTreeSortedMap(Map<K, V> map) {
        if (map == null || map.isEmpty()) return TreeSortedMap.newMap();
        return TreeSortedMap.newMap(map);
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param supplier Supplier<Map<K, V>>
     * @return MutableSortedMap<K, V>
     */
    public static <K, V> MutableSortedMap<K, V> newTreeSortedMap(Supplier<Map<K, V>> supplier) {
        if (supplier == null) return TreeSortedMap.newMap();
        return newTreeSortedMap(supplier.get());
    }

    /*
     * ******************** ConcurrentMutableMap ********************
     */

    /**
     * @param <K> Key type
     * @param <V> Value type
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap() {
        return ConcurrentHashMap.newMap();
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param capacity int
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(int capacity) {
        return ConcurrentHashMap.newMap(optimizationCapacity(capacity));
    }


    /**
     * @param <K> Key type
     * @param <V> Value type
     * @param map Map<K, V>
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Map<K, V> map) {
        if (map == null || map.isEmpty()) return ConcurrentHashMap.newMap();
        return ConcurrentHashMap.newMap(map);
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param supplier Supplier<Map<K, V>>
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMap(Supplier<Map<K, V>> supplier) {
        if (supplier == null) return ConcurrentHashMap.newMap();
        return newConcurrentHashMap(supplier.get());
    }

    /**
     * @param <K> Key type
     * @param <V> Value type
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe() {
        return ConcurrentHashMapUnsafe.newMap();
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param capacity int
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(int capacity) {
        return ConcurrentHashMapUnsafe.newMap(optimizationCapacity(capacity));
    }


    /**
     * @param <K> Key type
     * @param <V> Value type
     * @param map Map<K, V>
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Map<K, V> map) {
        if (map == null || map.isEmpty()) return ConcurrentHashMapUnsafe.newMap();
        return ConcurrentHashMapUnsafe.newMap(map);
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param supplier Supplier<Map<K, V>>
     * @return ConcurrentMutableMap<K, V>
     */
    public static <K, V> ConcurrentMutableMap<K, V> newConcurrentHashMapUnsafe(Supplier<Map<K, V>> supplier) {
        if (supplier == null) return ConcurrentHashMapUnsafe.newMap();
        return newConcurrentHashMapUnsafe(supplier.get());
    }

}
