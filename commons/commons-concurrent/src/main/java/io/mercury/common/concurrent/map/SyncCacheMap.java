package io.mercury.common.concurrent.map;

import org.jctools.maps.NonBlockingHashMap;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @param <K>
 * @param <V>
 * @author yellow013
 */
@ThreadSafe
public final class SyncCacheMap<K, V> {

    private final ConcurrentMap<K, Saved> valueMap = new NonBlockingHashMap<>();

    private final Function<K, V> refresher;

    private class Saved {

        private boolean available;
        private final V value;

        private Saved(boolean available, V value) {
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
