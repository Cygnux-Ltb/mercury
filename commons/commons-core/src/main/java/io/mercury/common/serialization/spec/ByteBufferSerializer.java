package io.mercury.common.serialization.spec;

import java.nio.ByteBuffer;

import io.mercury.common.serialization.Serializer;

@FunctionalInterface
public interface ByteBufferSerializer<T> extends Serializer<T, ByteBuffer> {
}
