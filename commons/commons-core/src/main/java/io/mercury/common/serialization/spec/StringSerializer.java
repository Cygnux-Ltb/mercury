package io.mercury.common.serialization.spec;

import io.mercury.common.serialization.Serializer;

@FunctionalInterface
public interface StringSerializer<T> extends Serializer<T, String> {
}
