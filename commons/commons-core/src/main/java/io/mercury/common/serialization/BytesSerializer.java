package io.mercury.common.serialization;

@FunctionalInterface
public interface BytesSerializer<T> extends Serializer<T, byte[]> {
}
