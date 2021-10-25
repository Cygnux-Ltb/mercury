package io.mercury.common.serialization;

import io.mercury.common.serialization.api.Deserializer;

@FunctionalInterface
public interface BytesDeserializer<R> extends Deserializer<byte[], R> {
}
