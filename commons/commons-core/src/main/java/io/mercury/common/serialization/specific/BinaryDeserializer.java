package io.mercury.common.serialization.specific;

import java.nio.ByteBuffer;

import io.mercury.common.serialization.Deserializer;

@FunctionalInterface
public interface BinaryDeserializer<T> extends Deserializer<ByteBuffer, T> {

}
