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
	 * @return ImmutableIntSetFactoryInstance
	 */
	public static final ImmutableIntSetFactory intSetFactory() {
		return ImmutableIntSetFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param values
	 * @return ImmutableIntSet
	 */
	public static final ImmutableIntSet newImmutableIntSet(@Nonnull int... values) {
		if (isNullOrEmpty(values))
			return ImmutableIntSetFactoryImpl.INSTANCE.empty();
		return ImmutableIntSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @return ImmutableLongSetFactoryInstance
	 */
	public static final ImmutableLongSetFactory longSetFactoryInstance() {
		return ImmutableLongSetFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param values
	 * @return ImmutableLongSet
	 */
	public static final ImmutableLongSet newImmutableLongSet(@Nonnull long... values) {
		if (isNullOrEmpty(values))
			return ImmutableLongSetFactoryImpl.INSTANCE.empty();
		return ImmutableLongSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @return ImmutableDoubleSetFactoryInstance
	 */
	public static final ImmutableDoubleSetFactory doubleSetFactory() {
		return ImmutableDoubleSetFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param values
	 * @return ImmutableDoubleSet
	 */
	public static final ImmutableDoubleSet newImmutableDoubleSet(@Nonnull double... values) {
		if (isNullOrEmpty(values))
			return ImmutableDoubleSetFactoryImpl.INSTANCE.empty();
		return ImmutableDoubleSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @return ImmutableSetFactoryInstance
	 */
	public static final ImmutableSetFactory objectSetFactory() {
		return ImmutableSetFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param <E>
	 * @param iterable
	 * @return ImmutableSet
	 */
	public static final <E> ImmutableSet<E> newImmutableSet(@Nonnull Iterable<E> iterable) {
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
	public static final <E> ImmutableSet<E> newImmutableSet(@Nonnull E... values) {
		return ImmutableSetFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @return ImmutableSortedSetFactory
	 */
	public static final ImmutableSortedSetFactory objSortedSetFactoryInstance() {
		return ImmutableSortedSetFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param <E>
	 * @param iterable
	 * @return ImmutableSortedSet
	 */
	public static final <E> ImmutableSortedSet<E> newImmutableSortedSet(@Nonnull Iterable<E> iterable) {
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
	public static final <E> ImmutableSortedSet<E> newImmutableSortedSet(@Nonnull E... values) {
		if (isNullOrEmpty(values))
			return ImmutableSortedSetFactoryImpl.INSTANCE.empty();
		return ImmutableSortedSetFactoryImpl.INSTANCE.with(values);
	}

}
