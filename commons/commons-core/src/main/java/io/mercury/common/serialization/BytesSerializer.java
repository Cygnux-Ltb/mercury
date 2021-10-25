package io.mercury.common.serialization;

import io.mercury.common.serialization.api.Serializer;

@FunctionalInterface
public interface BytesSerializer<T> extends Serializer<T, byte[]> {
}
