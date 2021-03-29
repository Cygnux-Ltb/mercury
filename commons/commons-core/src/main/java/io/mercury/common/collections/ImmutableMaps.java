package io.mercury.common.collections;

import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.block.function.primitive.IntFunction;
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
import org.eclipse.collections.api.map.primitive.ImmutableIntIntMap;
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
	 * @return ImmutableIntIntMapFactoryInstance
	 */
	@Nonnull
	public static final ImmutableIntIntMapFactory immutableIntIntMapFactory() {
		return ImmutableIntIntMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @return The new ImmutableIntIntMap
	 */
	public static final <T> ImmutableIntIntMap newImmutableIntIntMapFrom(@Nonnull Iterable<T> iterable,
			@Nonnull IntFunction<T> keyFunction, @Nonnull IntFunction<T> valueFunction) {
		return ImmutableIntIntMapFactoryImpl.INSTANCE.from(iterable, keyFunction, valueFunction);
	}

	/**
	 * 
	 * @return ImmutableIntLongMapFactoryInstance
	 */
	public static final ImmutableIntLongMapFactory immutableIntLongMapFactory() {
		return ImmutableIntLongMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @return ImmutableIntDoubleMapFactoryInstance
	 */
	public static final ImmutableIntDoubleMapFactory immutableIntDoubleMapFactory() {
		return ImmutableIntDoubleMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @return ImmutableIntObjectMapFactoryInstance
	 */
	public static final ImmutableIntObjectMapFactory immutableIntObjectMapFactory() {
		return ImmutableIntObjectMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @return ImmutableLongIntMapFactoryInstance
	 */
	public static final ImmutableLongIntMapFactory immutableLongIntMapFactory() {
		return ImmutableLongIntMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @return ImmutableLongLongMapFactoryInstance
	 */
	public static final ImmutableLongLongMapFactory immutableLongLongMapFactory() {
		return ImmutableLongLongMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @return ImmutableLongDoubleMapFactoryInstance
	 */
	public static final ImmutableLongDoubleMapFactory immutableLongDoubleMapFactory() {
		return ImmutableLongDoubleMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @return ImmutableLongObjectMapFactoryInstance
	 */
	public static final ImmutableLongObjectMapFactory immutableLongObjectMapFactory() {
		return ImmutableLongObjectMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @return ImmutableMapFactoryInstance
	 */
	public static final ImmutableMapFactory immutableMapFactory() {
		return ImmutableMapFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param key
	 * @param value
	 * @return The new ImmutableMap
	 */
	public static final <K, V> ImmutableMap<K, V> newImmutableMap(K key, V value) {
		return ImmutableMapFactoryImpl.INSTANCE.with(key, value);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return The new ImmutableMap
	 */
	public static final <K, V> ImmutableMap<K, V> newImmutableMap(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return ImmutableMapFactoryImpl.INSTANCE.empty();
		return ImmutableMapFactoryImpl.INSTANCE.withAll(map);
	}

	/**
	 * @param <Map<K,  V>>
	 * @param supplier
	 * @return The new ImmutableMap
	 */
	public static final <K, V> ImmutableMap<K, V> newImmutableMap(Supplier<Map<K, V>> supplier) {
		if (supplier == null)
			return newImmutableMap(MutableMaps.newUnifiedMap());
		return newImmutableMap(supplier.get());
	}

}
