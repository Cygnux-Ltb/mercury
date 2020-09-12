package io.mercury.common.codec.specific;

@FunctionalInterface
public interface TextDecoder<R> extends Codec<CharSequence, R> {

	R decode(CharSequence sequence);

	@Override
	default R codec(CharSequence t) {
		return decode(t);
	}

}
