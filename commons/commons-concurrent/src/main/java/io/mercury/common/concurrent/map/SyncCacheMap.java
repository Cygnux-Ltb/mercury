package io.mercury.common.concurrent.map;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.jctools.maps.NonBlockingHashMap;

/**
 * 
 * @author yellow013
 *
 * @param <K>
 * @param <V>
 */
@ThreadSafe
public final class SyncCacheMap<K, V> {

	private ConcurrentMap<K, Saved> valueMap = new NonBlockingHashMap<>();

	private Function<K, V> refresher;

	private class Saved {

		private volatile boolean available;
		private volatile V value;

		private Saved(boolean available, V value) {
			super();
			this.available = available;
			this.value = value;
		}
	}

	public SyncCacheMap(Function<K, V> refresher) {
		if (refresher == null)
			throw new IllegalArgumentException("refresher is can't null...");
		this.refresher = refresher;
	}

	public SyncCacheMap<K, V> put(@Nonnull K key, @Nonnull V value) {
		valueMap.put(key, new Saved(true, value));
		return this;
	}

	public Optional<V> get(@Nonnull K key) {
		Saved saved = valueMap.get(key);
		if (saved == null || !saved.available) {
			V refreshed = refresher.apply(key);
			return refreshed == null ? Optional.empty() : put(key, refreshed).get(key);
		} else
			// return saved.isAvailable ? Optional.of(saved.value) : get(key);
			return Optional.of(saved.value);
	}

	public SyncCacheMap<K, V> setUnavailable(@Nonnull K key) {
		Saved saved = valueMap.get(key);
		if (saved != null)
			saved.available = false;
		return this;
	}

	public SyncCacheMap<K, V> delete(@Nonnull K key) {
		valueMap.remove(key);
		return this;
	}

}
