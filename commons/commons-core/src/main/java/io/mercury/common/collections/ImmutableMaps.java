package io.mercury.common.collections;

import java.util.Map;
import java.util.SortedMap;
import java.util.function.Supplier;

import org.eclipse.collections.api.factory.map.ImmutableMapFactory;
import org.eclipse.collections.api.factory.map.primitive.ImmutableIntDoubleMapFactory;
import org.eclipse.collections.api.factory.map.primitive.ImmutableIntIntMapFactory;
import org.eclipse.collections.api.factory.map.primitive.ImmutableIntLongMapFactory;
import org.eclipse.collections.api.factory.map.primitive.ImmutableIntObjectMapFactory;
import org.eclipse.collections.api.factory.map.primitive.ImmutableLongDoubleMapFactory;
import org.eclipse.collections.api.factory.map.primitive.ImmutableLongIntMapFactory;
import org.eclipse.collections.api.factory.map.primitive.ImmutableLongLongMapFactory;
import org.eclipse.collections.api.factory.map.primitive.ImmutableLongObjectMapFactory;
import org.eclipse.collections.api.factory.map.sorted.ImmutableSortedMapFactory;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;
import org.eclipse.collections.impl.map.immutable.ImmutableMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntDoubleMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntIntMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntLongMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntObjectMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongDoubleMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongIntMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongLongMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongObjectMapFactoryImpl;
import org.eclipse.collections.impl.map.sorted.immutable.ImmutableSortedMapFactoryImpl;
import org.eclipse.collections.impl.map.sorted.mutable.TreeSortedMap;

public final class ImmutableMaps {

	private ImmutableMaps() {
	}

	/**
	 * @return ImmutableIntIntMapFactory Instance
	 */
	public static ImmutableIntIntMapFactory getIntIntMapFactory() {
		return ImmutableIntIntMapFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableIntLongMapFactory Instance
	 */
	public static ImmutableIntLongMapFactory getIntLongMapFactory() {
		return ImmutableIntLongMapFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableIntDoubleMapFactory Instance
	 */
	public static ImmutableIntDoubleMapFactory getIntDoubleMapFactory() {
		return ImmutableIntDoubleMapFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableIntObjectMapFactory Instance
	 */
	public static ImmutableIntObjectMapFactory getIntObjectMapFactory() {
		return ImmutableIntObjectMapFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableLongIntMapFactory Instance
	 */
	public static ImmutableLongIntMapFactory getLongIntMapFactory() {
		return ImmutableLongIntMapFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableLongLongMapFactory Instance
	 */
	public static ImmutableLongLongMapFactory getLongLongMapFactory() {
		return ImmutableLongLongMapFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableLongDoubleMapFactory Instance
	 */
	public static ImmutableLongDoubleMapFactory getLongDoubleMapFactory() {
		return ImmutableLongDoubleMapFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableLongObjectMapFactory Instance
	 */
	public static ImmutableLongObjectMapFactory getLongObjectMapFactory() {
		return ImmutableLongObjectMapFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableMapFactory Instance
	 */
	public static ImmutableMapFactory getMapFactory() {
		return ImmutableMapFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableSortedMapFactory Instance
	 */
	public static ImmutableSortedMapFactory getSortedMapFactory() {
		return ImmutableSortedMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param key
	 * @param value
	 * @return The new ImmutableMap
	 */
	public static <K, V> ImmutableMap<K, V> newImmutableMap(K key, V value) {
		return ImmutableMapFactoryImpl.INSTANCE.with(key, value);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return The new ImmutableMap
	 */
	public static <K, V> ImmutableMap<K, V> newImmutableMap(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return ImmutableMapFactoryImpl.INSTANCE.empty();
		return ImmutableMapFactoryImpl.INSTANCE.withAll(map);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param supplier
	 * @return The new ImmutableMap
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
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
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
	 * 
	 * @param <K>
	 * @param <V>
	 * @param supplier
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
