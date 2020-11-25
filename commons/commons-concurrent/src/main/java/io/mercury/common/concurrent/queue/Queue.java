package io.mercury.common.concurrent.queue;

import java.util.function.Predicate;

public interface Queue<E> {

	boolean enqueue(E e);

	String queueName();

	public static enum WaitingStrategy {
		SpinWaiting, SleepWaiting, Blocking
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