package io.mercury.persistence.rocksdb.map.entity;

public interface RocksReversibleKey extends RocksKey {

	/**
	 * 
	 * @return RocksDB reversed key byte[]
	 */
	byte[] reversedKey();

}
