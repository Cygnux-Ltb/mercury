package io.mercury.common.serialization;

import java.io.File;
import java.util.Collection;

@FunctionalInterface
public interface FileSerializer<T> extends Serializer<Collection<T>, File> {
}
