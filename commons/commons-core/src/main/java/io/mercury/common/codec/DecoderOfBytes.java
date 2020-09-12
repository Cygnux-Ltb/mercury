package io.mercury.common.codec.specific;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface BytesDecoder<R> extends Codec<ByteBuffer, R> {

	R decode(ByteBuffer buffer);

	@Override
	default R codec(ByteBuffer t) {
		return decode(t);
	}

}
