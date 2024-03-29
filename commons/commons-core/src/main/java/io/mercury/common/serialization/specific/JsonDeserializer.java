package io.mercury.common.serialization.specific;

import io.mercury.common.serialization.api.Deserializer;

@FunctionalInterface
public interface JsonDeserializer<R> extends Deserializer<String, R> {
}