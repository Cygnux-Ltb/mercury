package io.mercury.common.serialization;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface ByteBufferDeserializer<R> extends Deserializer<ByteBuffer, R> {
	
}
