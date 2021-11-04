package io.mercury.common.functional;

import java.util.function.Consumer;

/**
 * 
 * @author yellow013
 *
 */
@FunctionalInterface
public interface ExceptionHandler<E extends Exception> extends Consumer<E> {

	void handle(E e);

	@Override
	default void accept(E e) {
		handle(e);
	}

}
