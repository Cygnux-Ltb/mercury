package io.mercury.common.collections;

import static io.mercury.common.collections.Capacity.checkAndGet;

import java.util.Collection;
import java.util.Collections;
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

    /*
     * primitive int set
     *
     * @return
     */
    public static MutableIntSet newIntHashSet() {
        return new IntHashSet();
    }

    /**
     * primitive int set
     *
     * @param capacity
     * @return
     */
    public static MutableIntSet newIntHashSet(Capacity capacity) {
        return new IntHashSet(checkAndGet(capacity));
    }

    /**
     * primitive int set
     *
     * @param values
     * @return
     */
    public static MutableIntSet newIntHashSetWith(int... values) {
        if (ArrayUtil.isNullOrEmpty(values))
            return newIntHashSet();
        return new IntHashSet(values);
    }

    /**
     * primitive long set
     *
     * @return
     */
    public static MutableLongSet newLongHashSet() {
        return new LongHashSet();
    }

    /**
     * primitive long set
     *
     * @param capacity
     * @return
     */
    public static MutableLongSet newLongHashSet(Capacity capacity) {
        return new LongHashSet(checkAndGet(capacity));
    }

    /**
     * primitive long set
     *
     * @param values
     * @return
     */
    public static MutableLongSet newLongHashSetWith(long... values) {
        if (ArrayUtil.isNullOrEmpty(values))
            return newLongHashSet();
        return new LongHashSet(values);
    }

    /**
     * primitive double set
     *
     * @return
     */
    public static MutableDoubleSet newDoubleHashSet() {
        return new DoubleHashSet();
    }

    /**
     * primitive double set
     *
     * @param capacity
     * @return
     */
    public static MutableDoubleSet newDoubleHashSet(Capacity capacity) {
        return new DoubleHashSet(checkAndGet(capacity));
    }

    /**
     * primitive double set
     *
     * @param values
     * @return
     */
    public static MutableDoubleSet newDoubleHashSetWith(double... values) {
        if (ArrayUtil.isNullOrEmpty(values))
            return newDoubleHashSet();
        return new DoubleHashSet(values);
    }

    /**
     * Set
     *
     * @param <E>
     * @return
     */
    public static <E> MutableSet<E> newUnifiedSet() {
        return new UnifiedSet<>();
    }

    /**
     * Set
     *
     * @param <E>
     * @param values
     * @return
     */
    @SafeVarargs
    public static <E> MutableSet<E> newUnifiedSet(E... values) {
        MutableSet<E> set = new UnifiedSet<>();
        if (ArrayUtil.isNullOrEmpty(values))
            return set;
        Collections.addAll(set, values);
        return set;
    }

    /**
     * Set
     *
     * @param <E>
     * @param capacity
     * @return
     */
    public static <E> MutableSet<E> newUnifiedSet(Capacity capacity) {
        return new UnifiedSet<>(checkAndGet(capacity));
    }

    /**
     * Set
     *
     * @param <E>
     * @param iterator
     * @return
     */
    public static <E> MutableSet<E> newUnifiedSet(Iterator<E> iterator) {
        MutableSet<E> mutableSet = newUnifiedSet();
        if (iterator != null && iterator.hasNext())
            while (iterator.hasNext())
                mutableSet.add(iterator.next());
        return mutableSet;
    }

    /**
     * Set
     *
     * @param <E>
     * @param collection
     * @return
     */
    public static <E> MutableSet<E> newUnifiedSet(Collection<E> collection) {
        if (collection == null || collection.isEmpty())
            return newUnifiedSet();
        return new UnifiedSet<>(collection);
    }

    /**
     * SortedSet
     *
     * @param <E>
     * @return
     */
    public static <E> MutableSortedSet<E> newTreeSortedSet() {
        return new TreeSortedSet<>();
    }

    /**
     * SortedSet
     *
     * @param <E>
     * @param values
     * @return
     */
    @SafeVarargs
    public static <E> MutableSortedSet<E> newTreeSortedSet(E... values) {
        MutableSortedSet<E> set = new TreeSortedSet<>();
        if (ArrayUtil.isNullOrEmpty(values))
            return set;
        Collections.addAll(set, values);
        return set;
    }

    /**
     * SortedSet
     *
     * @param <E>
     * @param comparator
     * @return
     */
    public static <E> MutableSortedSet<E> newTreeSortedSet(Comparator<E> comparator) {
        if (comparator == null)
            return newTreeSortedSet();
        return new TreeSortedSet<>(comparator);
    }

    /**
     * SortedSet
     *
     * @param <E>
     * @param iterable
     * @return
     */
    public static <E> MutableSortedSet<E> newTreeSortedSet(Iterable<E> iterable) {
        if (Iterate.isEmpty(iterable))
            return newTreeSortedSet();
        return new TreeSortedSet<>(iterable);
    }

    /**
     * SortedSet
     *
     * @param <E>
     * @param comparator
     * @return
     */
    public static <E> MutableSortedSet<E> newTreeSortedSet(Comparator<E> comparator, Iterable<E> iterable) {
        if (comparator != null && iterable != null)
            return new TreeSortedSet<>(comparator, iterable);
        if (comparator != null)
            return newTreeSortedSet(comparator);
        if (iterable != null)
            return newTreeSortedSet(iterable);
        return newTreeSortedSet();
    }

}
