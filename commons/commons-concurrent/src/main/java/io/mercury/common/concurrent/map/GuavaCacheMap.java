package io.mercury.common.concurrent.map;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.mercury.common.log.CommonLoggerFactory;

public class GuavaCacheMap<K, V> {

	private final LoadingCache<K, V> cache;

	private static final Logger log = CommonLoggerFactory.getLogger(GuavaCacheMap.class);

	private GuavaCacheMap(CacheMapBuilder builder, Function<K, V> refresher) {
		this.cache = CacheBuilder.newBuilder().maximumSize(builder.maximumSize).expireAfterAccess(builder.duration)
				.build(new CacheLoader<K, V>() {
					@Override
					public V load(K key) throws Exception {
						return refresher.apply(key);
					}
				});
	}

	public static CacheMapBuilder builder() {
		return new CacheMapBuilder();
	}

	@Nonnull
	public Optional<V> get(K k) {
		try {
			V v = cache.get(k);
			return v != null ? Optional.of(v) : Optional.empty();
		} catch (ExecutionException e) {
			log.error("CacheMap.get -> [{}] throw {}", k, e.getMessage(), e);
			return Optional.empty();
		}
	}

	/**
	 * Builder for GuavaCacheMap
	 * 
	 * @author yellow013
	 *
	 */
	public static class CacheMapBuilder {

		private long maximumSize = 1024;
		private Duration duration = Duration.ofHours(2);

		public CacheMapBuilder maximumSize(long maximumSize) {
			this.maximumSize = maximumSize;
			return this;
		}

		public CacheMapBuilder expireAfterAccess(Duration duration) {
			this.duration = duration;
			return this;
		}

		public <K, V> GuavaCacheMap<K, V> buildWith(@Nonnull Function<K, V> refresher) {
			return new GuavaCacheMap<>(this, refresher);
		}

	}

}
