package io.mercury.common.concurrent.map;

import java.time.temporal.Temporal;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.list.MutableList;
import org.jctools.maps.NonBlockingHashMapLong;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.collections.MutableLists;

@NotThreadSafe
public abstract class ConcurrentTemporalMap<K extends Temporal, V, T extends ConcurrentTemporalMap<K, V, T>> {

	protected final ToLongFunction<K> keyFunc;

	private final Function<K, K> nextKeyFunc;

	private final BiPredicate<K, K> hasNextKey;

	private final ConcurrentMap<Long, V> savedMap;

	public ConcurrentTemporalMap(ToLongFunction<K> keyFunc, Function<K, K> nextKeyFunc, BiPredicate<K, K> hasNextKey) {
		this(keyFunc, nextKeyFunc, hasNextKey, 128);
	}

	public ConcurrentTemporalMap(ToLongFunction<K> keyFunc, Function<K, K> nextKeyFunc, BiPredicate<K, K> hasNextKey,
			int initialCapacity) {
		this.keyFunc = keyFunc;
		this.nextKeyFunc = nextKeyFunc;
		this.hasNextKey = hasNextKey;
		this.savedMap = new NonBlockingHashMapLong<>(initialCapacity);
	}

	/**
	 * abstract method
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@AbstractFunction
	public abstract T put(@Nonnull K key, V value);

	/**
	 * general get method
	 * 
	 * @param key
	 * @return
	 */
	public V get(@Nonnull K key) {
		return savedMap.get(keyFunc.applyAsLong(key));
	}

	/**
	 * general get method
	 * 
	 * @param startPoint
	 * @param endPoint
	 * @return
	 */
	public MutableList<V> scan(@Nonnull K startPoint, @Nonnull K endPoint) {
		MutableList<V> rtnList = MutableLists.newFastList(32);
		if (!hasNextKey.test(startPoint, endPoint))
			return putResult(rtnList, get(endPoint));
		putResult(rtnList, get(startPoint));
		K nextKey = nextKeyFunc.apply(startPoint);
		while (hasNextKey.test(nextKey, endPoint)) {
			putResult(rtnList, get(nextKey));
			nextKey = nextKeyFunc.apply(nextKey);
		}
		return rtnList;
	}

	private MutableList<V> putResult(MutableList<V> rtnList, V value) {
		if (value != null)
			rtnList.add(value);
		return rtnList;
	}

	protected ConcurrentMap<Long, V> getSavedMap() {
		return savedMap;
	}

	protected void put(long key, V value) {
		savedMap.put(key, value);
	}

}
