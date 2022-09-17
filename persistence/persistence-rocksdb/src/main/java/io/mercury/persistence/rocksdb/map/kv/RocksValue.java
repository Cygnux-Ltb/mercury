package io.mercury.persistence.rocksdb.map.kv;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public interface RocksValue {

    /**
     * @return RocksDB value ByteBuf
     */
    @Nonnull
    default ByteBuf value() {
        return value(Unpooled.buffer(valueLength()));
    }

    /**
     * @param buf ByteBuf
     * @return ByteBuf
     */
    @Nonnull
    ByteBuf value(@Nonnull ByteBuf buf);

    /**
     * @return int
     */
    int valueLength();

}
