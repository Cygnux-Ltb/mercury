package io.mercury.common.concurrent.cache;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.mercury.common.log.Log4j2LoggerFactory;

/**
 * 
 * @author yellow013
 *
 * @param <K>
 * @param <V>
 */
@ThreadSafe
public class CacheMap<K, V> {

	private final LoadingCache<K, V> cache;

	private static final Logger log = Log4j2LoggerFactory.getLogger(CacheMap.class);

	private CacheMap(CacheMapBuilder builder, Function<K, V> refresher) {
		this.cache = CacheBuilder.newBuilder().maximumSize(builder.maximumSize).expireAfterAccess(builder.duration)
				.build(new CacheLoader<K, V>() {
					@Override
					public V load(K key) throws Exception {
						return refresher.apply(key);
					}
				});
	}

	public static CacheMapBuilder newBuilder() {
		return new CacheMapBuilder();
	}

	@Nonnull
	public Optional<V> getOptional(K key) {
		try {
			V value = cache.get(key);
			return value != null ? Optional.of(value) : Optional.empty();
		} catch (ExecutionException e) {
			log.error("CacheMap.get -> [{}] throw {}", key, e.getMessage(), e);
			return Optional.empty();
		}
	}

	public void setUnavailable(K key) {
		cache.invalidate(key);
	}

	/**
	 * Builder for GuavaCacheMap
	 * 
	 * @author yellow013
	 *
	 */
	public static class CacheMapBuilder {

		private long maximumSize = 1024;
		private Duration duration = Duration.ofHours(8);

		public CacheMapBuilder maximumSize(long maximumSize) {
			this.maximumSize = maximumSize;
			return this;
		}

		public CacheMapBuilder expireAfterAccess(Duration duration) {
			this.duration = duration;
			return this;
		}

		public <K, V> CacheMap<K, V> build(@Nonnull Function<K, V> refresher) {
			return new CacheMap<>(this, refresher);
		}

	}

}
