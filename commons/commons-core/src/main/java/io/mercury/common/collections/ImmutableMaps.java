package io.mercury.common.collections;

import java.util.Map;
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
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.map.immutable.ImmutableMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntDoubleMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntIntMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntLongMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntObjectMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongDoubleMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongIntMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongLongMapFactoryImpl;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongObjectMapFactoryImpl;

public final class ImmutableMaps {

	private ImmutableMaps() {
	}

	/**
	 * 
	 * ImmutableIntIntMapFactory
	 */
	public static ImmutableIntIntMapFactory getIntIntMapFactory() {
		return ImmutableIntIntMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableIntLongMapFactory
	 */
	public static ImmutableIntLongMapFactory getIntLongMapFactory() {
		return ImmutableIntLongMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableIntDoubleMapFactory
	 */
	public static ImmutableIntDoubleMapFactory getIntDoubleMapFactory() {
		return ImmutableIntDoubleMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableIntObjectMapFactory
	 */
	public static ImmutableIntObjectMapFactory getIntObjectMapFactory() {
		return ImmutableIntObjectMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableLongIntMapFactory
	 */
	public static ImmutableLongIntMapFactory getLongIntMapFactory() {
		return ImmutableLongIntMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableLongLongMapFactory
	 */
	public static ImmutableLongLongMapFactory getLongLongMapFactory() {
		return ImmutableLongLongMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableLongDoubleMapFactory
	 */
	public static ImmutableLongDoubleMapFactory getLongDoubleMapFactory() {
		return ImmutableLongDoubleMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableLongObjectMapFactory
	 */
	public static ImmutableLongObjectMapFactory getLongObjectMapFactory() {
		return ImmutableLongObjectMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableMapFactory
	 */
	public static ImmutableMapFactory getMapFactory() {
		return ImmutableMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param key
	 * @param value
	 * @return
	 */
	public static <K, V> ImmutableMap<K, V> newImmutableMap(K key, V value) {
		return ImmutableMapFactoryImpl.INSTANCE.with(key, value);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V> ImmutableMap<K, V> newImmutableMap(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return ImmutableMapFactoryImpl.INSTANCE.empty();
		return ImmutableMapFactoryImpl.INSTANCE.withAll(map);
	}

	/**
	 * @param <Map<K,  V>>
	 * @param supplier
	 * @return ImmutableMap<K, V>
	 */
	public static <K, V> ImmutableMap<K, V> newImmutableMap(Supplier<Map<K, V>> supplier) {
		if (supplier == null)
			return newImmutableMap(MutableMaps.newUnifiedMap());
		return newImmutableMap(supplier.get());
	}

}
