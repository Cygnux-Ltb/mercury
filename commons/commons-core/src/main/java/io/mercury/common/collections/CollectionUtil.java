package io.mercury.common.collections;

import io.mercury.common.lang.Asserter;
import io.mercury.common.util.ArrayUtil;
import org.eclipse.collections.api.collection.ImmutableCollection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.IntFunction;

public final class CollectionUtil {

    private CollectionUtil() {
    }

    /**
     * @param <E>        type
     * @param collection Collection<E>
     * @return boolean
     */
    public static <E> boolean onlyOneElement(@Nullable Collection<E> collection) {
        return collection != null && collection.size() == 1;
    }

    /**
     * @param <E>        type
     * @param collection Collection<E>
     * @return boolean
     */
    public static <E> boolean onlyOneElement(@Nullable ImmutableCollection<E> collection) {
        return collection != null && collection.size() == 1;
    }

    /**
     * @param <E>        E
     * @param collection ImmutableList<E>
     * @return boolean
     */
    public static <E> boolean isNullOrEmpty(@Nullable Collection<E> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * @param <E>        E
     * @param collection ImmutableList<E>
     * @return boolean
     */
    public static <E> boolean isNullOrEmpty(@Nullable ImmutableCollection<E> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * @param <E>        type
     * @param collection Collection<E>
     * @return boolean
     */
    public static <E> boolean notEmpty(@Nullable Collection<E> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * @param <E>        E
     * @param collection ImmutableList<E>
     * @return boolean
     */
    public static <E> boolean notEmpty(@Nullable ImmutableCollection<E> collection) {
        return collection != null && collection.notEmpty();
    }

    /**
     * @param <E>        type
     * @param <C>        collection type
     * @param collection C
     * @param values     E
     * @return C
     */
    @SafeVarargs
    public static <E, C extends Collection<E>> C addAll(@Nonnull final C collection,
                                                        @Nonnull final E... values) {
        Asserter.nonNull(collection, "collection");
        if (ArrayUtil.isNullOrEmpty(values))
            return collection;
        Collections.addAll(collection, values);
        return collection;
    }

    /**
     * @param <C>        C type
     * @param <E>        E type
     * @param <V>        V type
     * @param collection C
     * @param converter  Function<V, E>
     * @param values     V array
     * @return C object
     */
    @SafeVarargs
    public static <E, V, C extends Collection<E>> C addAll(@Nonnull final C collection,
                                                           @Nonnull final Function<V, E> converter,
                                                           @Nonnull final V... values) {
        Asserter.nonNull(collection, "collection");
        Asserter.nonNull(converter, "converter");
        if (ArrayUtil.isNullOrEmpty(values))
            return collection;
        for (V v : values)
            collection.add(converter.apply(v));
        return collection;
    }

    /**
     * @param <E>          E type
     * @param collection   Collection<E>
     * @param arrayBuilder IntFunction<E[]>
     * @return E array
     */
    public static <E> E[] toArray(@Nonnull final Collection<E> collection,
                                  @Nonnull final IntFunction<E[]> arrayBuilder) {
        Asserter.nonNull(collection, "collection");
        E[] values = arrayBuilder.apply(collection.size());
        Iterator<E> iterator = collection.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            values[i] = iterator.next();
            i++;
        }
        return values;
    }

}
