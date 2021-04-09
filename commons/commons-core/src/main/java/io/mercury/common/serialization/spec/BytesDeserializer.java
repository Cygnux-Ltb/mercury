package io.mercury.common.serialization.spec;

import io.mercury.common.serialization.Deserializer;

@FunctionalInterface
public interface ByteArrayDeserializer<R> extends Deserializer<byte[], R> {

}
