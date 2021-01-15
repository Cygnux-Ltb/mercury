package io.mercury.common.serialization.spec;

import io.mercury.common.serialization.Deserializer;

@FunctionalInterface
public interface TextDeserializer<R> extends Deserializer<String, R> {

}
