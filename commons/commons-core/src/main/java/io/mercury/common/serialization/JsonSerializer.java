package io.mercury.common.serialization;

@FunctionalInterface
public interface JsonSerializer<T> extends Serializer<T, String> {
}
