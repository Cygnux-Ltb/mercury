package io.mercury.common.functional;

import java.util.function.BiFunction;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Converter<F, T> extends BiFunction<F, T, T> {

	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	@Nonnull
	T conversion(@Nonnull F from, @Nonnull T to);

	@Override
	default T apply(F from, T to) {
		return conversion(from, to);
	}

}
