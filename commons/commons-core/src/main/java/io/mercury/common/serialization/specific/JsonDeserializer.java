package io.mercury.common.serialization;

import io.mercury.common.serialization.base.Deserializer;

@FunctionalInterface
public interface JsonDeserializer<R> extends Deserializer<String, R> {
}
