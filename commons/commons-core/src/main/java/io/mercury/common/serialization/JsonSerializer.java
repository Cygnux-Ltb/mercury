package io.mercury.common.serialization;

import io.mercury.common.serialization.api.Serializer;

@FunctionalInterface
public interface JsonSerializer<T> extends Serializer<T, String> {
}
