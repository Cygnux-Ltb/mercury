package io.mercury.common.concurrent.cache;

import java.util.function.Consumer;

import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.concurrent.queue.AbstractSingleConsumerQueue;
import io.mercury.common.concurrent.queue.jct.JctSingleConsumerQueue;
import io.mercury.common.util.StringSupport;

/**
 * 
 * @author yellow013
 *
 * @param <K>
 * @param <V>
 */
@ThreadSafe
public final class AsyncCacheMap<K, V> {

	private final MutableMap<K, V> mutableMap = MutableMaps.newUnifiedMap(Capacity.L08_SIZE.value());

	private final MutableLongObjectMap<Consumer<V>> consumerMap = MutableMaps
			.newLongObjectHashMap(Capacity.L07_SIZE.value());

	private final String cacheName;

	private final AbstractSingleConsumerQueue<ExecEvent> execQueue;

	private final AbstractSingleConsumerQueue<QueryResult> queryQueue;

	// private ExecutorService executor = Executors.newSingleThreadExecutor();

	private final class ExecEvent {

		private long nanoTime;
		private K key;
		private V value;

		public ExecEvent(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public ExecEvent(K key, long nanoTime) {
			this.key = key;
			this.nanoTime = nanoTime;
		}

	}

	private final class QueryResult {

		private V value;
		private long nanoTime;

		public QueryResult(V value, long nanoTime) {
			this.value = value;
			this.nanoTime = nanoTime;
		}

	}

	public AsyncCacheMap(String cacheName) {
		this.cacheName = StringSupport.isNullOrEmpty(cacheName) ? "AsyncCacheMap-" + hashCode() : cacheName;
		this.execQueue = JctSingleConsumerQueue.multiProducer(this.cacheName + "-ExecQueue").setCapacity(64)
				.build(event -> asyncExec(event));
		this.queryQueue = JctSingleConsumerQueue.multiProducer(this.cacheName + "-QueryQueue").setCapacity(64)
				.build(result -> consumerMap.remove(result.nanoTime).accept(result.value));
	}

	private void asyncExec(ExecEvent event) {
		if (event.nanoTime == 0L) {
			if (event.value != null)
				mutableMap.put(event.key, event.value);
		} else {
			V v = mutableMap.get(event.key);
			queryQueue.enqueue(new QueryResult(v, event.nanoTime));
		}
	}

	public void asyncPut(K key, V value) {
		execQueue.enqueue(new ExecEvent(key, value));
	}

	public void asyncGet(K key, Consumer<V> consumer) {
		long nanoTime = System.nanoTime();
		consumerMap.put(nanoTime, consumer);
		execQueue.enqueue(new ExecEvent(key, nanoTime));
	}

	public static void main(String[] args) {
		AsyncCacheMap<Integer, String> asyncCacheMap = new AsyncCacheMap<>("TEST");
		for (int i = 0; i < 100; i++) {
			asyncCacheMap.asyncPut(i, i + "%%^");
			asyncCacheMap.asyncGet(i, v -> System.out.println(v));
		}
		System.out.println(1111);
	}

}
