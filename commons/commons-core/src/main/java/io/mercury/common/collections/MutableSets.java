package io.mercury.common.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.primitive.MutableDoubleSet;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.api.set.primitive.MutableLongSet;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.eclipse.collections.impl.set.mutable.primitive.DoubleHashSet;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;
import org.eclipse.collections.impl.set.mutable.primitive.LongHashSet;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.eclipse.collections.impl.utility.Iterate;

import io.mercury.common.util.ArrayUtil;

public final class MutableSets {

	private MutableSets() {
	}

	/**
	 * primitive int set
	 * 
	 * @return
	 */
	public static final MutableIntSet newIntHashSet() {
		return new IntHashSet();
	}

	/**
	 * primitive int set
	 * 
	 * @param capacity
	 * @return
	 */
	public static final MutableIntSet newIntHashSet(Capacity capacity) {
		return new IntHashSet(capacity.value());
	}

	/**
	 * primitive int set
	 * 
	 * @param values
	 * @return
	 */
	public static final MutableIntSet newIntHashSetWith(int... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return newIntHashSet();
		return new IntHashSet(values);
	}

	/**
	 * primitive long set
	 * 
	 * @return
	 */
	public static final MutableLongSet newLongHashSet() {
		return new LongHashSet();
	}

	/**
	 * primitive long set
	 * 
	 * @param capacity
	 * @return
	 */
	public static final MutableLongSet newLongHashSet(Capacity capacity) {
		return new LongHashSet(capacity.value());
	}

	/**
	 * primitive long set
	 * 
	 * @param values
	 * @return
	 */
	public static final MutableLongSet newLongHashSetWith(long... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return newLongHashSet();
		return new LongHashSet(values);
	}

	/**
	 * primitive double set
	 * 
	 * @return
	 */
	public static final MutableDoubleSet newDoubleHashSet() {
		return new DoubleHashSet();
	}

	/**
	 * primitive double set
	 * 
	 * @param capacity
	 * @return
	 */
	public static final MutableDoubleSet newDoubleHashSet(Capacity capacity) {
		return new DoubleHashSet(capacity.value());
	}

	/**
	 * primitive double set
	 * 
	 * @param values
	 * @return
	 */
	public static final MutableDoubleSet newDoubleHashSetWith(double... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return newDoubleHashSet();
		return new DoubleHashSet(values);
	}

	/**
	 * object set
	 * 
	 * @param <E>
	 * @return
	 */
	public static final <E> MutableSet<E> newUnifiedSet() {
		return new UnifiedSet<>();
	}

	/**
	 * object set
	 * 
	 * @param <E>
	 * @param values
	 * @return
	 */
	@SafeVarargs
	public static final <E> MutableSet<E> newUnifiedSet(E... values) {
		UnifiedSet<E> unifiedSet = new UnifiedSet<>();
		if (ArrayUtil.isNullOrEmpty(values))
			return unifiedSet;
		for (E e : values)
			unifiedSet.add(e);
		return unifiedSet;
	}

	/**
	 * object set
	 * 
	 * @param <E>
	 * @param capacity
	 * @return
	 */
	public static final <E> MutableSet<E> newUnifiedSet(Capacity capacity) {
		return new UnifiedSet<>(capacity.value());
	}

	/**
	 * set
	 * 
	 * @param <E>
	 * @param iterator
	 * @return
	 */
	public static final <E> MutableSet<E> newUnifiedSet(Iterator<E> iterator) {
		MutableSet<E> mutableSet = newUnifiedSet();
		if (iterator != null && iterator.hasNext())
			while (iterator.hasNext())
				mutableSet.add(iterator.next());
		return mutableSet;
	}

	/**
	 * set
	 * 
	 * @param <E>
	 * @param collection
	 * @return
	 */
	public static final <E> MutableSet<E> newUnifiedSet(Collection<E> collection) {
		if (collection == null || collection.isEmpty())
			return newUnifiedSet();
		return new UnifiedSet<>(collection);
	}

	/**
	 * sorted set
	 * 
	 * @param <E>
	 * @return
	 */
	public static final <E> MutableSortedSet<E> newTreeSortedSet() {
		return new TreeSortedSet<>();
	}

	/**
	 * sorted set
	 * 
	 * @param <E>
	 * @param comparator
	 * @return
	 */
	public static final <E> MutableSortedSet<E> newTreeSortedSet(Comparator<E> comparator) {
		if (comparator == null)
			return newTreeSortedSet();
		return new TreeSortedSet<>(comparator);
	}

	/**
	 * sorted set
	 * 
	 * @param <E>
	 * @param iterable
	 * @return
	 */
	public static final <E> MutableSortedSet<E> newTreeSortedSet(Iterable<E> iterable) {
		if (Iterate.isEmpty(iterable))
			return newTreeSortedSet();
		return new TreeSortedSet<>(iterable);
	}

	/**
	 * sorted set
	 * 
	 * @param <E>
	 * @param comparator
	 * @return
	 */
	public static final <E> MutableSortedSet<E> newTreeSortedSet(Comparator<E> comparator, Iterable<E> iterable) {
		if (comparator != null && iterable != null)
			return new TreeSortedSet<>(comparator, iterable);
		if (comparator != null)
			return newTreeSortedSet(comparator);
		if (iterable != null)
			return newTreeSortedSet(iterable);
		return newTreeSortedSet();
	}

}
