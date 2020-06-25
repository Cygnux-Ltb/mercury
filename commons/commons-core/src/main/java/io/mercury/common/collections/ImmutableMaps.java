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
	public static ImmutableIntIntMapFactory IntIntMapFactory() {
		return ImmutableIntIntMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableIntLongMapFactory
	 */
	public static ImmutableIntLongMapFactory IntLongMapFactory() {
		return ImmutableIntLongMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableIntDoubleMapFactory
	 */
	public static ImmutableIntDoubleMapFactory IntDoubleMapFactory() {
		return ImmutableIntDoubleMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableIntObjectMapFactory
	 */
	public static ImmutableIntObjectMapFactory IntObjectMapFactory() {
		return ImmutableIntObjectMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableLongIntMapFactory
	 */
	public static ImmutableLongIntMapFactory LongIntMapFactory() {
		return ImmutableLongIntMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableLongLongMapFactory
	 */
	public static ImmutableLongLongMapFactory LongLongMapFactory() {
		return ImmutableLongLongMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableLongDoubleMapFactory
	 */
	public static ImmutableLongDoubleMapFactory LongDoubleMapFactory() {
		return ImmutableLongDoubleMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * ImmutableLongObjectMapFactory
	 */
	public static ImmutableLongObjectMapFactory LongObjectMapFactory() {
		return ImmutableLongObjectMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * MapFactory
	 */
	public static ImmutableMapFactory MapFactory() {
		return ImmutableMapFactoryImpl.INSTANCE;
	}

	public static <K, V> ImmutableMap<K, V> newMap(K key, V value) {
		if (key == null || value == null)
			return ImmutableMapFactoryImpl.INSTANCE.empty();
		return ImmutableMapFactoryImpl.INSTANCE.with(key, value);
	}

	public static <K, V> ImmutableMap<K, V> newMap(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return ImmutableMapFactoryImpl.INSTANCE.empty();
		return ImmutableMapFactoryImpl.INSTANCE.withAll(map);
	}

	public static <K, V> ImmutableMap<K, V> newMap(Supplier<Map<K, V>> supplier) {
		if (supplier == null)
			return newMap(MutableMaps.newUnifiedMap());
		return newMap(supplier.get());
	}

}
