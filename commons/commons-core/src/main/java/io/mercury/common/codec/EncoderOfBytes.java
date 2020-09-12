package io.mercury.common.codec.specific;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface BytesEncoder<T> extends Codec<T, ByteBuffer> {

	ByteBuffer encode(T t);
	
	@Override
	default ByteBuffer codec(T t) {
		return encode(t);
	}
	
}
