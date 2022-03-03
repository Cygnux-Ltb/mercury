package io.mercury.common.serialization;

import io.mercury.common.serialization.base.Deserializer;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface ByteBufferDeserializer<R> extends Deserializer<ByteBuffer, R> {
	
}
