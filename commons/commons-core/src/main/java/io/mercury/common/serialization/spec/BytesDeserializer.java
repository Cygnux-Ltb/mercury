package io.mercury.common.serialization.spec;

import io.mercury.common.serialization.Deserializer;

@FunctionalInterface
public interface BytesDeserializer<R> extends Deserializer<byte[], R> {
}
