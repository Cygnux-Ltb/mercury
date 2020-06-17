package io.mercury.common.codec.specific;

import java.util.function.Function;

@FunctionalInterface
interface Codec<T, R> extends Function<T, R> {

	R codec(T t);

	@Override
	default R apply(T t) {
		return codec(t);
	}

}