package io.mercury.persistence.rocksdb.map.kv;

public interface RocksReversibleKey extends RocksKey {

	/**
	 * 
	 * @return RocksDB reversed key byte[]
	 */
	byte[] reversedKey();

}
