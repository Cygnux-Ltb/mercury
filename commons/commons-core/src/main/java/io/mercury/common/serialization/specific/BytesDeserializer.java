package io.mercury.common.serialization.specific;

import io.mercury.common.serialization.basic.Deserializer;

@FunctionalInterface
public interface BytesDeserializer<R> extends Deserializer<byte[], R> {
}
