package io.mercury.common.codec;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface EncoderOfBytes<T> extends Encoder<T, ByteBuffer> {

}
