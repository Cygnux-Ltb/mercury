package io.mercury.common.collections;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.factory.set.ImmutableSetFactory;
import org.eclipse.collections.api.factory.set.primitive.ImmutableDoubleSetFactory;
import org.eclipse.collections.api.factory.set.primitive.ImmutableIntSetFactory;
import org.eclipse.collections.api.factory.set.primitive.ImmutableLongSetFactory;
import org.eclipse.collections.api.factory.set.sorted.ImmutableSortedSetFactory;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.primitive.ImmutableDoubleSet;
import org.eclipse.collections.api.set.primitive.ImmutableIntSet;
import org.eclipse.collections.api.set.primitive.ImmutableLongSet;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;
import org.eclipse.collections.impl.set.immutable.ImmutableSetFactoryImpl;
import org.eclipse.collections.impl.set.immutable.primitive.ImmutableDoubleSetFactoryImpl;
import org.eclipse.collections.impl.set.immutable.primitive.ImmutableIntSetFactoryImpl;
import org.eclipse.collections.impl.set.immutable.primitive.ImmutableLongSetFactoryImpl;
import org.eclipse.collections.impl.set.sorted.immutable.ImmutableSortedSetFactoryImpl;

import io.mercury.common.util.ArrayUtil;

public final class ImmutableSets {

	private ImmutableSets() {
	}

	/**
	 * @return ImmutableSetFactory Instance
	 */
	public static ImmutableSetFactory getSetFactory() {
		return ImmutableSetFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableSortedSetFactory Instance
	 */
	public static ImmutableSortedSetFactory getSortedSetFactory() {
		return ImmutableSortedSetFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableIntSetFactory Instance
	 */
	public static ImmutableIntSetFactory getIntSetFactory() {
		return ImmutableIntSetFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableLongSetFactory Instance
	 */
	public static ImmutableLongSetFactory getLongSetFactory() {
		return ImmutableLongSetFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @return ImmutableDoubleSetFactory Instance
	 */
	public static ImmutableDoubleSetFactory getDoubleSetFactory() {
		return ImmutableDoubleSetFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param values
	 * @return ImmutableIntSet
	 */
	public static ImmutableIntSet newImmutableIntSet(@Nonnull int... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return ImmutableIntSetFactoryImpl.INSTANCE.empty();
		return ImmutableIntSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @param values
	 * @return ImmutableLongSet
	 */
	public static ImmutableLongSet newImmutableLongSet(@Nonnull long... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return ImmutableLongSetFactoryImpl.INSTANCE.empty();
		return ImmutableLongSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @param values
	 * @return ImmutableDoubleSet
	 */
	public static ImmutableDoubleSet newImmutableDoubleSet(@Nonnull double... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return ImmutableDoubleSetFactoryImpl.INSTANCE.empty();
		return ImmutableDoubleSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @param <E>
	 * @param iterable
	 * @return ImmutableSet
	 */
	public static <E> ImmutableSet<E> newImmutableSet(@Nonnull Iterable<E> iterable) {
		if (iterable == null)
			return ImmutableSetFactoryImpl.INSTANCE.empty();
		return ImmutableSetFactoryImpl.INSTANCE.withAll(iterable);
	}

	/**
	 * 
	 * @param <E>
	 * @param values
	 * @return ImmutableSet
	 */
	@SafeVarargs
	public static <E> ImmutableSet<E> newImmutableSet(@Nonnull E... values) {
		return ImmutableSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @param <E>
	 * @param iterable
	 * @return ImmutableSortedSet
	 */
	public static <E> ImmutableSortedSet<E> newImmutableSortedSet(@Nonnull Iterable<E> iterable) {
		if (iterable == null)
			return ImmutableSortedSetFactoryImpl.INSTANCE.empty();
		return ImmutableSortedSetFactoryImpl.INSTANCE.withAll(iterable);
	}

	/**
	 * 
	 * @param <E>
	 * @param values
	 * @return ImmutableSortedSet
	 */
	@SafeVarargs
	public static <E> ImmutableSortedSet<E> newImmutableSortedSet(@Nonnull E... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return ImmutableSortedSetFactoryImpl.INSTANCE.empty();
		return ImmutableSortedSetFactoryImpl.INSTANCE.with(values);
	}

}
