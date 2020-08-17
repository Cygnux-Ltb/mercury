package io.mercury.persistence.rocksdb.map.entity;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public interface RocksValue {

	/**
	 * 
	 * @return RocksDB value ByteBuf
	 */
	@Nonnull
	default ByteBuf value() {
		return value(Unpooled.buffer(valueLength()));
	}

	/**
	 * 
	 * @param useBuf
	 * @return
	 */
	@Nonnull
	ByteBuf value(@Nonnull ByteBuf useBuf);

	/**
	 * 
	 * @return
	 */
	int valueLength();

}
