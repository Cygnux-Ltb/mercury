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
	 * primitive set
	 */
	public static MutableIntSet newIntHashSet() {
		return new IntHashSet();
	}

	public static MutableIntSet newIntHashSet(Capacity capacity) {
		return new IntHashSet(capacity.size());
	}

	public static MutableIntSet newIntHashSetWith(int... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return newIntHashSet();
		return new IntHashSet(values);
	}

	public static MutableLongSet newLongHashSet() {
		return new LongHashSet();
	}

	public static MutableLongSet newLongHashSet(Capacity capacity) {
		return new LongHashSet(capacity.size());
	}

	public static MutableLongSet newLongHashSetWith(long... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return newLongHashSet();
		return new LongHashSet(values);
	}

	public static MutableDoubleSet newDoubleHashSet() {
		return new DoubleHashSet();
	}

	public static MutableDoubleSet newDoubleHashSet(Capacity capacity) {
		return new DoubleHashSet(capacity.size());
	}

	public static MutableDoubleSet newDoubleHashSetWith(double... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return newDoubleHashSet();
		return new DoubleHashSet(values);
	}

	/**
	 * set
	 */
	public static <E> MutableSet<E> newUnifiedSet() {
		return new UnifiedSet<>();
	}

	@SafeVarargs
	public static <E> MutableSet<E> newUnifiedSet(E... values) {
		UnifiedSet<E> unifiedSet = new UnifiedSet<>();
		if (ArrayUtil.isNullOrEmpty(values))
			return unifiedSet;
		for (E e : values)
			unifiedSet.add(e);
		return unifiedSet;
	}

	public static <E> MutableSet<E> newUnifiedSet(Capacity capacity) {
		return new UnifiedSet<>(capacity.size());
	}

	public static <E> MutableSet<E> newUnifiedSet(Iterator<E> iterator) {
		MutableSet<E> mutableSet = newUnifiedSet();
		if (iterator != null && iterator.hasNext())
			while (iterator.hasNext())
				mutableSet.add(iterator.next());
		return mutableSet;
	}

	public static <E> MutableSet<E> newUnifiedSet(Collection<E> collection) {
		if (collection == null || collection.isEmpty())
			return newUnifiedSet();
		return new UnifiedSet<>(collection);
	}

	public static <E> MutableSortedSet<E> newTreeSortedSet() {
		return new TreeSortedSet<>();
	}

	public static <E> MutableSortedSet<E> newTreeSortedSet(Iterable<E> iterable) {
		if (Iterate.isEmpty(iterable))
			return newTreeSortedSet();
		return new TreeSortedSet<>(iterable);
	}

	public static <E> MutableSortedSet<E> newTreeSortedSet(Comparator<E> comparator) {
		if (comparator == null)
			return newTreeSortedSet();
		return new TreeSortedSet<>(comparator);
	}

}
