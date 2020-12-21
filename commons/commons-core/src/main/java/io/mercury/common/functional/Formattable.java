package io.mercury.common.functional;

import java.util.function.Supplier;

@FunctionalInterface
public interface Formattable<T> extends Supplier<T> {

	T format();

	@Override
	default T get() {
		return format();
	}

}
