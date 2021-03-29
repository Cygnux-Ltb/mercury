package io.mercury.common.collections;

import java.util.Comparator;

import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.api.multimap.sortedset.MutableSortedSetMultimap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.multimap.list.FastListMultimap;
import org.eclipse.collections.impl.multimap.set.UnifiedSetMultimap;
import org.eclipse.collections.impl.multimap.set.sorted.TreeSortedSetMultimap;

import io.mercury.common.util.ArrayUtil;

public final class Multimaps {

	private Multimaps() {
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return MutableListMultimap
	 */
	public static final <K, V> MutableListMultimap<K, V> newFastListMultimap() {
		return FastListMultimap.newMultimap();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param iterable
	 * @return MutableListMultimap
	 */
	public static final <K, V> MutableListMultimap<K, V> newFastListMultimap(Iterable<Pair<K, V>> iterable) {
		if (iterable == null)
			return FastListMultimap.newMultimap();
		return FastListMultimap.newMultimap(iterable);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param pairs
	 * @return MutableListMultimap
	 */
	@SafeVarargs
	public static final <K, V> MutableListMultimap<K, V> newFastListMultimap(Pair<K, V>... pairs) {
		if (ArrayUtil.isNullOrEmpty(pairs))
			return FastListMultimap.newMultimap();
		return FastListMultimap.newMultimap(pairs);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return MutableSetMultimap
	 */
	public static final <K, V> MutableSetMultimap<K, V> newUnifiedSetMultimap() {
		return UnifiedSetMultimap.newMultimap();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param iterable
	 * @return MutableSetMultimap
	 */
	public static final <K, V> MutableSetMultimap<K, V> newUnifiedSetMultimap(Iterable<Pair<K, V>> iterable) {
		if (iterable == null)
			return UnifiedSetMultimap.newMultimap();
		return UnifiedSetMultimap.newMultimap(iterable);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param pairs
	 * @return MutableSetMultimap
	 */
	@SafeVarargs
	public static final <K, V> MutableSetMultimap<K, V> newUnifiedSetMultimap(Pair<K, V>... pairs) {
		if (ArrayUtil.isNullOrEmpty(pairs))
			return UnifiedSetMultimap.newMultimap();
		return UnifiedSetMultimap.newMultimap(pairs);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return MutableSortedSetMultimap
	 */
	public static final <K, V> MutableSortedSetMultimap<K, V> newTreeSortedSetMultimap() {
		return TreeSortedSetMultimap.newMultimap();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param comparator
	 * @return MutableSortedSetMultimap
	 */
	public static final <K, V> MutableSortedSetMultimap<K, V> newTreeSortedSetMultimap(Comparator<V> comparator) {
		if (comparator == null)
			return TreeSortedSetMultimap.newMultimap();
		return TreeSortedSetMultimap.newMultimap(comparator);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param iterable
	 * @return MutableSortedSetMultimap
	 */
	public static final <K, V> MutableSortedSetMultimap<K, V> newTreeSortedSetMultimap(Iterable<Pair<K, V>> iterable) {
		if (iterable == null)
			return TreeSortedSetMultimap.newMultimap();
		return TreeSortedSetMultimap.newMultimap(iterable);
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param pairs
	 * @return MutableSortedSetMultimap
	 */
	@SafeVarargs
	public static final <K, V> MutableSortedSetMultimap<K, V> newTreeSortedSetMultimap(Pair<K, V>... pairs) {
		if (ArrayUtil.isNullOrEmpty(pairs))
			return TreeSortedSetMultimap.newMultimap();
		return TreeSortedSetMultimap.newMultimap(pairs);
	}

}
