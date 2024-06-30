package io.mercury.common.collections;

import io.mercury.common.util.ArrayUtil;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.map.immutable.ImmutableMapFactoryImpl;
import org.eclipse.collections.impl.map.sorted.immutable.ImmutableSortedMapFactoryImpl;
import org.eclipse.collections.impl.map.sorted.mutable.TreeSortedMap;

import java.util.Map;
import java.util.SortedMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ImmutableMaps {

    INSTANCE;

    /**
     * @param <K>   Key type
     * @param <V>   Value type
     * @param key   key
     * @param value value
     * @return The new ImmutableMap<K, V>
     */
    public static <K, V> ImmutableMap<K, V> newImmutableMap(K key, V value) {
        return ImmutableMapFactoryImpl.INSTANCE.with(key, value);
    }


    /**
     * @param pairs Pair<K, V> array
     * @param <K>   Key type
     * @param <V>   Value type
     * @return The new ImmutableMap<K, V>
     */
    @SafeVarargs
    public static <K, V> ImmutableMap<K, V> newImmutableMap(Pair<K, V>... pairs) {
        if (ArrayUtil.isNullOrEmpty(pairs))
            return ImmutableMapFactoryImpl.INSTANCE.empty();
        switch (pairs.length) {
            case 1 -> {
                return ImmutableMapFactoryImpl.INSTANCE.with(
                        pairs[0].getOne(), pairs[0].getTwo());
            }
            case 2 -> {
                return ImmutableMapFactoryImpl.INSTANCE.with(
                        pairs[0].getOne(), pairs[0].getTwo(),
                        pairs[1].getOne(), pairs[1].getTwo());
            }
            case 3 -> {
                return ImmutableMapFactoryImpl.INSTANCE.with(
                        pairs[0].getOne(), pairs[0].getTwo(),
                        pairs[1].getOne(), pairs[1].getTwo(),
                        pairs[2].getOne(), pairs[2].getTwo());
            }
            case 4 -> {
                return ImmutableMapFactoryImpl.INSTANCE.with(
                        pairs[0].getOne(), pairs[0].getTwo(),
                        pairs[1].getOne(), pairs[1].getTwo(),
                        pairs[2].getOne(), pairs[2].getTwo(),
                        pairs[3].getOne(), pairs[3].getTwo());
            }
            default -> {
                return newImmutableMap(Stream.of(pairs)
                        .collect(Collectors.toMap(Pair::getOne, Pair::getTwo)));
            }
        }
    }

    /**
     * @param <K> Key type
     * @param <V> Value type
     * @param map Map<K, V>
     * @return The new ImmutableMap<K, V>
     */
    public static <K, V> ImmutableMap<K, V> newImmutableMap(Map<K, V> map) {
        if (map == null || map.isEmpty())
            return ImmutableMapFactoryImpl.INSTANCE.empty();
        return ImmutableMapFactoryImpl.INSTANCE.withAll(map);
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param supplier Map<K, V> Supplier
     * @return The new ImmutableMap<K, V>
     */
    @SuppressWarnings("unchecked")
    public static <K, V> ImmutableMap<K, V> newImmutableMap(Supplier<Map<K, V>> supplier) {
        if (supplier == null)
            return ImmutableMapFactoryImpl.INSTANCE.empty();
        Map<K, V> map = supplier.get();
        if (map == null)
            return ImmutableMapFactoryImpl.INSTANCE.empty();
        if (map instanceof ImmutableMap)
            return (ImmutableMap<K, V>) map;
        return newImmutableMap(map);
    }

    /**
     * @param <K> Key type
     * @param <V> Value type
     * @param map Map<K, V>
     * @return ImmutableSortedMap<K, V>
     */
    public static <K, V> ImmutableSortedMap<K, V> newImmutableSortedMap(Map<K, V> map) {
        if (map == null)
            return ImmutableSortedMapFactoryImpl.INSTANCE.empty();
        if (map instanceof SortedMap)
            return ImmutableSortedMapFactoryImpl.INSTANCE.withSortedMap((SortedMap<K, V>) map);
        else
            return ImmutableSortedMapFactoryImpl.INSTANCE.withSortedMap(TreeSortedMap.newMap(map));
    }

    /**
     * @param <K>      Key type
     * @param <V>      Value type
     * @param supplier Map<K, V> supplier
     * @return ImmutableSortedMap<K, V>
     */
    public static <K, V> ImmutableSortedMap<K, V> newImmutableSortedMap(Supplier<Map<K, V>> supplier) {
        if (supplier == null)
            return ImmutableSortedMapFactoryImpl.INSTANCE.empty();
        Map<K, V> map = supplier.get();
        if (map == null)
            return ImmutableSortedMapFactoryImpl.INSTANCE.empty();
        if (map instanceof SortedMap)
            return ImmutableSortedMapFactoryImpl.INSTANCE.withSortedMap((SortedMap<K, V>) map);
        else
            return ImmutableSortedMapFactoryImpl.INSTANCE.withSortedMap(TreeSortedMap.newMap(map));
    }

}
