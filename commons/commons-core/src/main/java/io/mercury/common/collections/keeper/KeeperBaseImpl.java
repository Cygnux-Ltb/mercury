package io.mercury.common.collections.customize;

import static io.mercury.common.collections.MutableMaps.newConcurrentHashMap;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.map.ConcurrentMutableMap;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.collections.Capacity;

@ThreadSafe
public abstract class BaseKeeper<K, V> implements Keeper<K, V> {

	protected final ConcurrentMutableMap<K, V> savedMap;

	protected BaseKeeper() {
		this(Capacity.L06_SIZE_64);
	}

	protected BaseKeeper(Capacity capacity) {
		this.savedMap = newConcurrentHashMap(capacity);
	}

	@Nonnull
	public V acquire(@Nonnull K k) {
		return savedMap.getIfAbsentPutWithKey(k, this::createWithKey);
	}

	@CheckForNull
	public V get(@Nonnull K k) {
		return savedMap.get(k);
	}

	@AbstractFunction
	protected abstract V createWithKey(K k);

}
