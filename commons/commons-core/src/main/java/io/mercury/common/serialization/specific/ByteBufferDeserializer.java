package io.mercury.common.serialization.specific;

import io.mercury.common.serialization.api.Deserializer;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface ByteBufferDeserializer<R> extends Deserializer<ByteBuffer, R> {
}