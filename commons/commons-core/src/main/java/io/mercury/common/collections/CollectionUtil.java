package io.mercury.common.collections;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.IntFunction;

import javax.annotation.Nonnull;

import io.mercury.common.util.ArrayUtil;
import io.mercury.common.util.Assertor;

public final class CollectionUtil {

	/**
	 * 
	 * @param <E>
	 * @param coll
	 * @return
	 */
	public static final <E> boolean onlyOneElement(Collection<E> coll) {
		if (coll != null && coll.size() == 1)
			return true;
		return false;
	}

	/**
	 * 
	 * @param <E>
	 * @param coll
	 * @return
	 */
	public static final <E> boolean notEmpty(Collection<E> coll) {
		if (coll != null && coll.size() > 1)
			return true;
		return false;
	}

	/**
	 * 
	 * @param <E>
	 * @param collection
	 * @param values
	 * @return
	 */
	@SafeVarargs
	public static final <E, C extends Collection<E>> C addAll(@Nonnull final C collection, @Nonnull final E... values) {
		Assertor.nonNull(collection, "collection");
		if (ArrayUtil.isNullOrEmpty(values))
			return collection;
		for (var e : values)
			collection.add(e);
		return collection;
	}

	/**
	 * 
	 * @param <E>
	 * @param <V>
	 * @param collection
	 * @param converter
	 * @param values
	 * @return
	 */
	@SafeVarargs
	public static final <E, V, C extends Collection<E>> C addAll(@Nonnull final C collection,
			@Nonnull Function<V, E> converter, @Nonnull final V... values) {
		Assertor.nonNull(collection, "collection");
		Assertor.nonNull(converter, "converter");
		if (ArrayUtil.isNullOrEmpty(values))
			return collection;
		for (var v : values)
			collection.add(converter.apply(v));
		return collection;
	}

	public static final <E, C extends Collection<E>> E[] toArray(@Nonnull final C collection,
			IntFunction<E[]> arrayCreator) {
		Assertor.nonNull(collection, "collection");
		var values = arrayCreator.apply(collection.size());
		var iterator = collection.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			values[i] = iterator.next();
			i++;
		}
		return values;
	}

}
