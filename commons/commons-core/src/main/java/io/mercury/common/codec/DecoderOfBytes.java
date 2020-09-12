package io.mercury.common.codec;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface DecoderOfBytes<R> extends Decoder<ByteBuffer, R> {

}
