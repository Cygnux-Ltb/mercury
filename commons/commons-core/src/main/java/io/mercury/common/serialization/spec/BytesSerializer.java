package io.mercury.common.serialization.spec;

import io.mercury.common.serialization.Serializer;

@FunctionalInterface
public interface BytesSerializer<T> extends Serializer<T, byte[]> {
}
