package io.mercury.persistence.rocksdb.map.kv;

import javax.annotation.Nonnull;

public interface RocksKey {

	/**
	 * 
	 * @return RocksDB columnFamily byte[]
	 */
	byte[] columnFamily();

	/**
	 * 
	 * @return RocksDB primary key byte[]
	 */
	@Nonnull
	byte[] key();

	/**
	 * 
	 * @return
	 */
	default int length() {
		return key().length;
	}

}
