package io.mercury.common.collections;

import io.mercury.common.lang.Asserter;
import io.mercury.common.util.ArrayUtil;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.IntFunction;

public final class CollectionUtil {

    private CollectionUtil() {
    }

    /**
     * @param <E>
     * @param collection
     * @return
     */
    public static <E> boolean onlyOneElement(Collection<E> collection) {
        return collection != null && collection.size() == 1;
    }

    /**
     * @param <E>
     * @param collection
     * @return
     */
    public static <E> boolean notEmpty(Collection<E> collection) {
        return collection != null && collection.size() >= 1;
    }

    /**
     * @param <E>
     * @param collection
     * @param values
     * @return
     */
    @SafeVarargs
    public static <E, C extends Collection<E>> C addAll(@Nonnull final C collection, @Nonnull final E... values) {
        Asserter.nonNull(collection, "collection");
        if (ArrayUtil.isNullOrEmpty(values))
            return collection;
        Collections.addAll(collection, values);
        return collection;
    }

    /**
     * @param <E>
     * @param <V>
     * @param collection
     * @param converter
     * @param values
     * @return
     */
    @SafeVarargs
    public static <E, V, C extends Collection<E>> C addAll(@Nonnull final C collection,
                                                           @Nonnull Function<V, E> converter, @Nonnull final V... values) {
        Asserter.nonNull(collection, "collection");
        Asserter.nonNull(converter, "converter");
        if (ArrayUtil.isNullOrEmpty(values))
            return collection;
        for (V v : values)
            collection.add(converter.apply(v));
        return collection;
    }

    /**
     * @param <E>
     * @param collection
     * @param arrayCreator
     * @return
     */
    public static <E> E[] toArray(@Nonnull final Collection<E> collection,
                                  @Nonnull final IntFunction<E[]> arrayCreator) {
        Asserter.nonNull(collection, "collection");
        E[] values = arrayCreator.apply(collection.size());
        Iterator<E> iterator = collection.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            values[i] = iterator.next();
            i++;
        }
        return values;
    }

}
