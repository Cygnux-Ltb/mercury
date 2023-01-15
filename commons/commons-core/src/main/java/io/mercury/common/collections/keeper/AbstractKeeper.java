package io.mercury.common.collections.keeper;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.collections.Capacity;
import org.eclipse.collections.api.map.ConcurrentMutableMap;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import static io.mercury.common.collections.MutableMaps.newConcurrentHashMap;

/**
 * 
 * @author yellow013
 *
 * @param <K>
 * @param <V>
 */
@ThreadSafe
public abstract class AbstractKeeper<K, V> implements Keeper<K, V> {

	protected final ConcurrentMutableMap<K, V> savedMap;

	protected AbstractKeeper() {
		this(Capacity.L06_SIZE);
	}

	protected AbstractKeeper(Capacity capacity) {
		this.savedMap = newConcurrentHashMap(capacity.value());
	}

	@Nonnull
	public V acquire(@Nonnull K key) {
		return savedMap.getIfAbsentPutWithKey(key, this::createWithKey);
	}

	@CheckForNull
	public V get(@Nonnull K key) {
		return savedMap.get(key);
	}

	@AbstractFunction
	protected abstract V createWithKey(K key);

}
