package io.mercury.common.serialization;

@FunctionalInterface
public interface JsonDeserializer<R> extends Deserializer<String, R> {
}
