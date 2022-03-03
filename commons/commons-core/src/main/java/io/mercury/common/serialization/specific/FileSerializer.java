package io.mercury.common.serialization.specific;

import io.mercury.common.serialization.basic.Serializer;

import java.io.File;

@FunctionalInterface
public interface FileSerializer<T> extends Serializer<T, File> {
}