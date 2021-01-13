package io.mercury.common.collections.keeper;

import static io.mercury.common.collections.MutableMaps.newConcurrentHashMap;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.map.ConcurrentMutableMap;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.collections.Capacity;

/**
 * 
 * @author yellow013
 *
 * @param <K>
 * @param <V>
 */
@ThreadSafe
public abstract class KeeperBaseImpl<K, V> implements Keeper<K, V> {

	protected final ConcurrentMutableMap<K, V> savedMap;

	protected KeeperBaseImpl() {
		this(Capacity.L06_SIZE);
	}

	protected KeeperBaseImpl(Capacity capacity) {
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
