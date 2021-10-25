package io.mercury.common.serialization;

import java.nio.ByteBuffer;

import io.mercury.common.serialization.api.Deserializer;

@FunctionalInterface
public interface ByteBufferDeserializer<R> extends Deserializer<ByteBuffer, R> {
	
}
