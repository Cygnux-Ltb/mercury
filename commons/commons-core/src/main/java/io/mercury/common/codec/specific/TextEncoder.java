package io.mercury.common.codec.specific;

@FunctionalInterface
public interface TextEncoder<T> extends Codec<T, CharSequence> {

	CharSequence encode(T t);

	@Override
	default CharSequence codec(T t) {
		return encode(t);
	}

}
