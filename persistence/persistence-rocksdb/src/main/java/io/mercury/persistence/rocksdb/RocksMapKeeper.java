package io.mercury.persistence.rocksdb;

import io.mercury.common.collections.customize.BaseKeeper;
import io.mercury.persistence.rocksdb.map.RocksMap;
import io.mercury.persistence.rocksdb.map.entity.RocksKey;
import io.mercury.persistence.rocksdb.map.entity.RocksValue;

public class RocksMapKeeper<K extends RocksKey, V extends RocksValue> extends BaseKeeper<String, RocksMap<K, V>> {

	@Override
	protected RocksMap<K, V> createWithKey(String k) {
		// TODO Auto-generated method stub
		return null;
	}

}
