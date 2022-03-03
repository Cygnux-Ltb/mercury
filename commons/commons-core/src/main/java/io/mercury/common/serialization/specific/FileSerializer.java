package io.mercury.common.serialization;

import io.mercury.common.serialization.base.Serializer;

import java.io.File;
import java.util.Collection;

@FunctionalInterface
public interface FileSerializer<T> extends Serializer<Collection<T>, File> {
}
