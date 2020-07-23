package io.mercury.common.serialization;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface BinaryDeserializer<T> extends Deserializer<ByteBuffer, T> {

}
