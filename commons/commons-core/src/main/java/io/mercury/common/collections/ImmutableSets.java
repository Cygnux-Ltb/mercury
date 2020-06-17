package io.mercury.common.collections;

import static io.mercury.common.util.ArrayUtil.isNullOrEmpty;

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

public final class ImmutableSets {

	private ImmutableSets() {
	}

	/**
	 * immutable int set
	 */
	public static ImmutableIntSetFactory IntSetFactory() {
		return ImmutableIntSetFactoryImpl.INSTANCE;
	}

	public static ImmutableIntSet newIntSet(int i) {
		return ImmutableIntSetFactoryImpl.INSTANCE.with(i);
	}

	public static ImmutableIntSet newIntSet(@Nonnull int... values) {
		if (isNullOrEmpty(values))
			return ImmutableIntSetFactoryImpl.INSTANCE.empty();
		return ImmutableIntSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * immutable long set
	 */
	public static ImmutableLongSetFactory LongSetFactory() {
		return ImmutableLongSetFactoryImpl.INSTANCE;
	}

	public static ImmutableLongSet newLongSet(long l) {
		return ImmutableLongSetFactoryImpl.INSTANCE.with(l);
	}

	public static ImmutableLongSet newLongSet(@Nonnull long... values) {
		if (isNullOrEmpty(values))
			return ImmutableLongSetFactoryImpl.INSTANCE.empty();
		return ImmutableLongSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * immutable double set
	 */
	public static ImmutableDoubleSetFactory DoubleSetFactory() {
		return ImmutableDoubleSetFactoryImpl.INSTANCE;
	}

	public static ImmutableDoubleSet newDoubleSet(double d) {
		return ImmutableDoubleSetFactoryImpl.INSTANCE.with(d);
	}

	public static ImmutableDoubleSet newDoubleSet(@Nonnull double... values) {
		if (isNullOrEmpty(values))
			return ImmutableDoubleSetFactoryImpl.INSTANCE.empty();
		return ImmutableDoubleSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * immutable set
	 */
	public static ImmutableSetFactory SetFactory() {
		return ImmutableSetFactoryImpl.INSTANCE;
	}

	public static <E> ImmutableSet<E> newSet(@Nonnull Iterable<E> iterable) {
		if (iterable == null)
			return ImmutableSetFactoryImpl.INSTANCE.empty();
		return ImmutableSetFactoryImpl.INSTANCE.withAll(iterable);
	}

	public static <E> ImmutableSet<E> newSet(E e) {
		if (e == null)
			return ImmutableSetFactoryImpl.INSTANCE.empty();
		return ImmutableSetFactoryImpl.INSTANCE.with(e);
	}

	@SafeVarargs
	public static <E> ImmutableSet<E> newSet(@Nonnull E... values) {
		if (isNullOrEmpty(values))
			return ImmutableSetFactoryImpl.INSTANCE.empty();
		return ImmutableSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * immutable sorted set
	 */
	public static ImmutableSortedSetFactory SortedSetFactory() {
		return ImmutableSortedSetFactoryImpl.INSTANCE;
	}

	public static <E> ImmutableSortedSet<E> newSortedSet(@Nonnull Iterable<E> iterable) {
		if (iterable == null)
			return ImmutableSortedSetFactoryImpl.INSTANCE.empty();
		return ImmutableSortedSetFactoryImpl.INSTANCE.withAll(iterable);
	}

	@SafeVarargs
	public static <E> ImmutableSortedSet<E> newSortedSet(@Nonnull E... values) {
		if (isNullOrEmpty(values))
			return ImmutableSortedSetFactoryImpl.INSTANCE.empty();
		return ImmutableSortedSetFactoryImpl.INSTANCE.with(values);
	}

	public static void main(String[] args) {

		for (int i = 1; i < 32; i++) {
			System.out.println("1 << " + i + " : " + (1 << i));
		}
		System.out.println(Integer.MAX_VALUE);

	}

}
