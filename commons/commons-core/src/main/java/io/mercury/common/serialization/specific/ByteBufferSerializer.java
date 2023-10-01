package io.mercury.common.serialization.specific;

import io.mercury.common.serialization.api.Serializer;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface ByteBufferSerializer<T> extends Serializer<T, ByteBuffer> {
}
