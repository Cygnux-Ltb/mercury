package io.mercury.common.serialization.specific;

import io.mercury.common.serialization.basic.Serializer;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface ByteBufferSerializer<T> extends Serializer<T, ByteBuffer> {
}
