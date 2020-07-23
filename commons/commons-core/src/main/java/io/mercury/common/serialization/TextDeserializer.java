package io.mercury.common.serialization.specific;

import io.mercury.common.serialization.Deserializer;

@FunctionalInterface
public interface TextDeserializer<T> extends Deserializer<String, T> {

}
