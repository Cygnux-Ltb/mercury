package io.mercury.common.collections.map;

import java.time.temporal.Temporal;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.collections.MutableMaps;

@NotThreadSafe
public abstract class TemporalMap<K extends Temporal, V, T extends TemporalMap<K, V, T>> {

	protected ToLongFunction<K> keyToLangFunc;

	private Function<K, K> nextKeyFunc;

	private BiPredicate<K, K> hasNextKey;

	private MutableLongObjectMap<V> savedMap;

	public TemporalMap(ToLongFunction<K> keyToLangFunc, Function<K, K> nextKeyFunc, BiPredicate<K, K> hasNextKey) {
		this(keyToLangFunc, nextKeyFunc, hasNextKey, Capacity.L07_SIZE_128);
	}

	public TemporalMap(ToLongFunction<K> keyToLangFunc, Function<K, K> nextKeyFunc, BiPredicate<K, K> hasNextKey,
			Capacity capacity) {
		this.keyToLangFunc = keyToLangFunc;
		this.nextKeyFunc = nextKeyFunc;
		this.hasNextKey = hasNextKey;
		this.savedMap = MutableMaps.newLongObjectHashMap(capacity);
	}

	/**
	 * abstract method
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	abstract public T put(@Nonnull K key, V value);

	/**
	 * general get method
	 * 
	 * @param key
	 * @return
	 */
	public V get(@Nonnull K key) {
		return savedMap.get(keyToLangFunc.applyAsLong(key));
	}

	/**
	 * general get method
	 * 
	 * @param startPoint
	 * @param endPoint
	 * @return
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

	private MutableList<V> loadResult(MutableList<V> rtnList, V value) {
		if (value != null)
			rtnList.add(value);
		return rtnList;
	}

	protected MutableLongObjectMap<V> getSavedMap() {
		return savedMap;
	}

	protected void put(long key, V value) {
		savedMap.put(key, value);
	}

}
