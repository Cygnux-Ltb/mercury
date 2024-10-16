package io.mercury.persistence.rocksdb;

import io.mercury.common.collections.keeper.AbstractKeeper;
import io.mercury.persistence.rocksdb.map.RocksMap;
import io.mercury.persistence.rocksdb.map.kv.RocksKey;
import io.mercury.persistence.rocksdb.map.kv.RocksValue;

public class RocksMapKeeper<K extends RocksKey, V extends RocksValue>
        extends AbstractKeeper<String, RocksMap<K, V>> {

    @Override
    protected RocksMap<K, V> createWithKey(String k) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() {
        
    }

}
