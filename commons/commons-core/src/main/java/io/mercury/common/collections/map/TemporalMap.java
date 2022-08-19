package io.mercury.common.collections.map;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.collections.MutableMaps;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.time.temporal.Temporal;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToLongFunction;

@NotThreadSafe
public abstract class TemporalMap<K extends Temporal, V, T extends TemporalMap<K, V, T>> {

    /**
     * 获取Key
     */
    private final ToLongFunction<K> keyFunc;

    /**
     * 获取下一个Key
     */
    private final Function<K, K> nextKeyFunc;

    /**
     * Scan判断是否有下一个Key
     */
    private final BiPredicate<K, K> hasNextKey;

    /**
     *
     */
    private final MutableLongObjectMap<V> savedMap;

    public TemporalMap(ToLongFunction<K> keyFunc, Function<K, K> nextKeyFunc, BiPredicate<K, K> hasNextKey) {
        this(keyFunc, nextKeyFunc, hasNextKey, Capacity.L07_SIZE);
    }

    public TemporalMap(ToLongFunction<K> keyFunc, Function<K, K> nextKeyFunc, BiPredicate<K, K> hasNextKey,
                       Capacity capacity) {
        this.keyFunc = keyFunc;
        this.nextKeyFunc = nextKeyFunc;
        this.hasNextKey = hasNextKey;
        this.savedMap = MutableMaps.newLongObjectHashMap(capacity.value());
    }

    protected abstract T self();

    /**
     * @param key   K
     * @param value V
     * @return T
     */
    public T put(@Nonnull K key, V value) {
        savedMap.put(keyFunc.applyAsLong(key), value);
        return self();
    }

    /**
     * general get method
     *
     * @param key K
     * @return V
     */
    public V get(@Nonnull K key) {
        return savedMap.get(keyFunc.applyAsLong(key));
    }

    /**
     * range scan
     *
     * @param startPoint K
     * @param endPoint   K
     * @return MutableList<V>
     */
    public MutableList<V> scan(@Nonnull K startPoint, @Nonnull K endPoint) {
        MutableList<V> result = MutableLists.newFastList(32);
        if (!hasNextKey.test(startPoint, endPoint))
            return loadResult(result, get(endPoint));
        loadResult(result, get(startPoint));
        K nextKey = nextKeyFunc.apply(startPoint);
        while (hasNextKey.test(nextKey, endPoint)) {
            loadResult(result, get(nextKey));
            nextKey = nextKeyFunc.apply(nextKey);
        }
        return result;
    }

    /**
     * @param list  MutableList<V>
     * @param value V
     * @return MutableList<V>
     */
    private MutableList<V> loadResult(MutableList<V> list, V value) {
        list.add(value);
        return list;
    }

    /**
     *
     */
    protected MutableLongObjectMap<V> savedMap() {
        return savedMap;
    }

}
