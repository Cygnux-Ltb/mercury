package io.mercury.common.serialization.spec;

import io.mercury.common.serialization.Deserializer;

@FunctionalInterface
public interface StringDeserializer<R> extends Deserializer<String, R> {

}
