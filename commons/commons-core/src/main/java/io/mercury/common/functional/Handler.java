package io.mercury.common.functional;

import java.util.function.Consumer;

import io.mercury.common.annotation.thread.MustBeThreadSafe;

@FunctionalInterface
public interface Handler<E> extends Consumer<E> {

	@MustBeThreadSafe
	void handle(E e);

	@Override
	default void accept(E e) {
		handle(e);
	}

}
