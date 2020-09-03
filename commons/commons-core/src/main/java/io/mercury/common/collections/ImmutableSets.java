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
	 * 
	 * @return ImmutableIntSetFactory
	 */
	public static ImmutableIntSetFactory getIntSetFactory() {
		return ImmutableIntSetFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param i
	 * @return ImmutableIntSet
	 */
	public static ImmutableIntSet newImmutableIntSet(int i) {
		return ImmutableIntSetFactoryImpl.INSTANCE.with(i);
	}

	/**
	 * 
	 * @param values
	 * @return ImmutableIntSet
	 */
	public static ImmutableIntSet newImmutableIntSet(@Nonnull int... values) {
		if (isNullOrEmpty(values))
			return ImmutableIntSetFactoryImpl.INSTANCE.empty();
		return ImmutableIntSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @return ImmutableLongSetFactory
	 */
	public static ImmutableLongSetFactory getLongSetFactory() {
		return ImmutableLongSetFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param l
	 * @return ImmutableLongSet
	 */
	public static ImmutableLongSet newImmutableLongSet(long l) {
		return ImmutableLongSetFactoryImpl.INSTANCE.with(l);
	}

	/**
	 * 
	 * @param values
	 * @return ImmutableLongSet
	 */
	public static ImmutableLongSet newImmutableLongSet(@Nonnull long... values) {
		if (isNullOrEmpty(values))
			return ImmutableLongSetFactoryImpl.INSTANCE.empty();
		return ImmutableLongSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @return ImmutableDoubleSetFactory
	 */
	public static ImmutableDoubleSetFactory getDoubleSetFactory() {
		return ImmutableDoubleSetFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param d
	 * @return ImmutableDoubleSet
	 */
	public static ImmutableDoubleSet newImmutableDoubleSet(double d) {
		return ImmutableDoubleSetFactoryImpl.INSTANCE.with(d);
	}

	/**
	 * 
	 * @param values
	 * @return ImmutableDoubleSet
	 */
	public static ImmutableDoubleSet newImmutableDoubleSet(@Nonnull double... values) {
		if (isNullOrEmpty(values))
			return ImmutableDoubleSetFactoryImpl.INSTANCE.empty();
		return ImmutableDoubleSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @return ImmutableSetFactory
	 */
	public static ImmutableSetFactory getSetFactory() {
		return ImmutableSetFactoryImpl.INSTANCE;
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
	 * @param e
	 * @return ImmutableSet
	 */
	public static <E> ImmutableSet<E> newImmutableSet(E e) {
		if (e == null)
			return ImmutableSetFactoryImpl.INSTANCE.empty();
		return ImmutableSetFactoryImpl.INSTANCE.with(e);
	}

	/**
	 * 
	 * @param <E>
	 * @param values
	 * @return ImmutableSet
	 */
	@SafeVarargs
	public static <E> ImmutableSet<E> newImmutableSet(@Nonnull E... values) {
		if (isNullOrEmpty(values))
			return ImmutableSetFactoryImpl.INSTANCE.empty();
		return ImmutableSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @return ImmutableSortedSetFactory
	 */
	public static ImmutableSortedSetFactory SortedSetFactory() {
		return ImmutableSortedSetFactoryImpl.INSTANCE;
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
