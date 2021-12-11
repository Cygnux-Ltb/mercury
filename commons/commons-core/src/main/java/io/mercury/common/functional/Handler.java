package io.mercury.common.functional;

import java.util.function.Consumer;

@FunctionalInterface
public interface Handler<E> extends Consumer<E> {

	void handle(E e);

	@Override
	default void accept(E e) {
		handle(e);
	}

}
