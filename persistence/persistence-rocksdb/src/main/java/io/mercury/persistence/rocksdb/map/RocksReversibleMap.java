package io.mercury.persistence.rocksdb.map;

import java.util.Collection;

import org.rocksdb.Options;

import io.mercury.common.thread.ThreadSupport;
import io.mercury.persistence.rocksdb.map.kv.RocksReversibleKey;
import io.mercury.persistence.rocksdb.map.kv.RocksValue;

public class RocksReversibleMap<K extends RocksReversibleKey, V extends RocksValue> {

    public static void main(String[] args) {

        try (Options options = new Options()) {
            Runtime.getRuntime()
                    .addShutdownHook(ThreadSupport.newThread(
                            "RocksContainerCloseThread", options::close));
        }

    }

    public V get(K key) {
        return null;
    }

    public V get(byte[] key) {
        return null;
    }

    public V put(K key, V value) {
        return value;
    }

    public Collection<V> scan() {
        return null;
    }

}
