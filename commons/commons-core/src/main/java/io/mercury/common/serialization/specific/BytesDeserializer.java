package io.mercury.common.serialization.specific;

import io.mercury.common.serialization.api.Deserializer;

@FunctionalInterface
public interface BytesDeserializer<R> extends Deserializer<byte[], R> {
}
