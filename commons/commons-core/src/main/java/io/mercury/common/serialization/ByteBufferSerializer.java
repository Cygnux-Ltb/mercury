package io.mercury.common.serialization;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface ByteBufferSerializer<T> extends Serializer<T, ByteBuffer> {
}
