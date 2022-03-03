package io.mercury.common.serialization.specific;

import io.mercury.common.serialization.basic.Serializer;

@FunctionalInterface
public interface BytesSerializer<T> extends Serializer<T, byte[]> {
}