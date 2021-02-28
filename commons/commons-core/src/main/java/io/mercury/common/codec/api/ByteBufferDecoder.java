package io.mercury.common.codec.api;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface ByteBufferDecoder<R> extends Decoder<ByteBuffer, R> {

}
