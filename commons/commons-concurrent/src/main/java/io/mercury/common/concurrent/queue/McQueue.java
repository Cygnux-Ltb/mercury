package io.mercury.common.concurrent.queue.base;

import java.util.function.Predicate;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import io.mercury.common.concurrent.queue.Queue;

public interface McQueue<E> extends Queue<E> {

	@CheckForNull
	E dequeue();

	default E poll() {
		return dequeue();
	}

	default boolean pollAndApply(@Nonnull PollFunction<E> function) {
		return function.apply(dequeue());
	}

	@FunctionalInterface
	public static interface PollFunction<E> extends Predicate<E> {

		boolean apply(E e);

		@Override
		default boolean test(E e) {
			return apply(e);
		}

	}

}
