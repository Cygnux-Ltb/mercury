package io.mercury.common.concurrent.map;

//import java.time.Duration;
//import java.util.concurrent.ExecutionException;
//
//import org.slf4j.Logger;
//
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;
//
//import io.ffreedom.common.log.CommonLoggerFactory;

@Deprecated
public class GuavaCacheMap<K, V> {

//	
//	private LoadingCache<K, V> loadingCache;
//	private CacheRefresher<K, V> refresher;
//
//	private long maximumSize;
//	private Duration duration;
//
//	private Logger log = CommonLoggerFactory.getLogger(GuavaCacheMap.class);
//
//	private GuavaCacheMap(CacheMapBuilder builder, CacheRefresher<K, V> refresher) {
//		this.maximumSize = builder.maximumSize;
//		this.duration = builder.duration;
//		this.refresher = refresher;
//		initLoadingCache();
//	}
//
//	private void initLoadingCache() {
//		loadingCache = CacheBuilder.newBuilder().maximumSize(maximumSize).expireAfterAccess(duration)
//				.build(new CacheLoader<K, V>() {
//					@Override
//					public V load(K key) throws Exception {
//						return refresher == null ? null : refresher.refresh(key);
//					}
//				});
//	}
//
//	public static CacheMapBuilder builder() {
//		return new CacheMapBuilder();
//	}
//
//	public V get(K k) {
//		try {
//			return loadingCache.get(k);
//		} catch (ExecutionException e) {
//			logger.error("GuavaCacheMap.get -> [{}] : {}", k, e.getMessage(), e);
//			return null;
//		}
//	}
//
//	/**
//	 * Builder for GuavaCacheMap
//	 * 
//	 * @author yellow013
//	 *
//	 */
//	public static class CacheMapBuilder {
//		private long maximumSize = 1024;
//		private Duration duration = Duration.ofHours(2);
//
//		public CacheMapBuilder maximumSize(long maximumSize) {
//			this.maximumSize = maximumSize;
//			return this;
//		}
//
//		public CacheMapBuilder expireAfterAccess(Duration duration) {
//			this.duration = duration;
//			return this;
//		}
//
//		public <K, V> GuavaCacheMap<K, V> build(CacheRefresher<K, V> refresher) {
//			return new GuavaCacheMap<>(this, refresher);
//		}
//
//		public <K, V> GuavaCacheMap<K, V> build() {
//			return new GuavaCacheMap<>(this, null);
//		}
//
//	}

	/**
	 * Test Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
