package io.mercury.common.serialization.specific;

import io.mercury.common.serialization.api.Deserializer;

import java.io.File;

@FunctionalInterface
public interface FileDeserializer<T> extends Deserializer<File, T> {
}