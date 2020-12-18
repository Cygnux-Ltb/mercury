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

	protected final ConcurrentMutableMap<K, V> keeperMap;

	protected KeeperBaseImpl() {
		this(Capacity.L06_SIZE_64);
	}

	protected KeeperBaseImpl(Capacity capacity) {
		this.keeperMap = newConcurrentHashMap(capacity);
	}

	@Nonnull
	public V acquire(@Nonnull K k) {
		return keeperMap.getIfAbsentPutWithKey(k, this::createWithKey);
	}

	@CheckForNull
	public V get(@Nonnull K k) {
		return keeperMap.get(k);
	}

	@AbstractFunction
	protected abstract V createWithKey(K k);

}
