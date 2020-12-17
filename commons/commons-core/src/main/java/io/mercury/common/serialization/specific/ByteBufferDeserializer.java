package io.mercury.common.serialization;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface BinaryDeserializer<R> extends Deserializer<ByteBuffer, R> {

}
