package io.mercury.common.concurrent.cache;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.concurrent.queue.SingleConsumerQueue;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;

import javax.annotation.concurrent.ThreadSafe;
import java.util.function.Consumer;

import static io.mercury.common.concurrent.queue.SingleConsumerQueueWithJCT.mpscQueue;
import static io.mercury.common.util.StringSupport.isNullOrEmpty;

/**
 * @param <K>
 * @param <V>
 * @author yellow013
 */
@ThreadSafe
public final class AsyncCacheMap<K, V> {

    private final MutableMap<K, V> mutableMap = MutableMaps
            .newUnifiedMap(Capacity.HEX_100.size());

    private final MutableLongObjectMap<Consumer<V>> consumerMap = MutableMaps
            .newLongObjectMap(Capacity.HEX_80.size());

    private final String cacheName;

    private final SingleConsumerQueue<ExecEvent> execQueue;

    private final SingleConsumerQueue<QueryResult<V>> queryQueue;

    // private ExecutorService executor = Executors.newSingleThreadExecutor();

    private final class ExecEvent {
        private final K key;
        private V value;
        private long nanoTime;

        public ExecEvent(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public ExecEvent(K key, long nanoTime) {
            this.key = key;
            this.nanoTime = nanoTime;
        }
    }

    private record QueryResult<V>(
            V value,
            long nanoTime
    ) {
    }

    /**
     * @param cacheName String
     */
    public AsyncCacheMap(String cacheName) {
        this.cacheName = isNullOrEmpty(cacheName) ? "AsyncCacheMap-" + hashCode() : cacheName;
        this.execQueue = mpscQueue(this.cacheName + "-ExecQueue")
                .capacity(64).process(this::asyncExec);
        this.queryQueue = mpscQueue(this.cacheName + "-QueryQueue")
                .capacity(64).process(result -> consumerMap.remove(result.nanoTime).accept(result.value));
    }

    public String getCacheName() {
        return cacheName;
    }

    private void asyncExec(ExecEvent event) {
        if (event.nanoTime == 0L) {
            if (event.value != null)
                mutableMap.put(event.key, event.value);
        } else {
            V v = mutableMap.get(event.key);
            queryQueue.enqueue(new QueryResult<>(v, event.nanoTime));
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
            asyncCacheMap.asyncGet(i, System.out::println);
        }
        System.out.println(1111);
    }

}
