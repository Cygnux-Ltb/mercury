package io.mercury.common.serialization;

import java.nio.ByteBuffer;

import io.mercury.common.serialization.api.Serializer;

@FunctionalInterface
public interface ByteBufferSerializer<T> extends Serializer<T, ByteBuffer> {
}
