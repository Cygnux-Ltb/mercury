package io.mercury.common.serialization;

import io.mercury.common.serialization.base.Deserializer;

@FunctionalInterface
public interface BytesDeserializer<R> extends Deserializer<byte[], R> {
}
