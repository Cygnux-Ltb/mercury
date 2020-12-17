package io.mercury.common.serialization.specific;

import io.mercury.common.serialization.Serializer;

@FunctionalInterface
public interface ByteArraySerializer<T> extends Serializer<T, byte[]> {

}
