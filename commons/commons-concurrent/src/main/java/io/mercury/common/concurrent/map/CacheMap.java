package io.mercury.common.concurrent.map;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import javax.annotation.CheckForNull;

import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.mercury.common.log.CommonLoggerFactory;

public class CacheMap<K, V> {

	private LoadingCache<K, V> cache;
	private Function<K, V> refresher;

	private long maximumSize;
	private Duration duration;

	private static final Logger log = CommonLoggerFactory.getLogger(CacheMap.class);

	private CacheMap(CacheMapBuilder builder, Function<K, V> refresher) {
		this.maximumSize = builder.maximumSize;
		this.duration = builder.duration;
		this.refresher = refresher;
		initLoadingCache();
	}

	private void initLoadingCache() {
		this.cache = CacheBuilder.newBuilder().maximumSize(maximumSize).expireAfterAccess(duration)
				.build(new CacheLoader<K, V>() {
					@Override
					public V load(K key) throws Exception {
						return refresher == null ? null : refresher.apply(key);
					}
				});
	}

	public static CacheMapBuilder builder() {
		return new CacheMapBuilder();
	}

	@CheckForNull
	public V get(K k) {
		try {
			return cache.get(k);
		} catch (ExecutionException e) {
			log.error("GuavaCacheMap.get -> [{}] : {}", k, e.getMessage(), e);
			return null;
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

		public <K, V> CacheMap<K, V> build(Function<K, V> refresher) {
			return new CacheMap<>(this, refresher);
		}

		public <K, V> CacheMap<K, V> build() {
			return new CacheMap<>(this, null);
		}

	}

	/**
	 * Test Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
