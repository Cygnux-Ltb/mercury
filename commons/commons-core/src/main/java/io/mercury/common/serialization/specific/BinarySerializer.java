package io.mercury.common.serialization.specific;

import java.nio.ByteBuffer;

import io.mercury.common.serialization.Serializer;

@FunctionalInterface
public interface BinarySerializer<T> extends Serializer<T, ByteBuffer> {

}
