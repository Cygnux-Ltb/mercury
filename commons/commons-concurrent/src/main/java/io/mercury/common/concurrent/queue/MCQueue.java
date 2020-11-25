package io.mercury.common.concurrent.queue;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface MCQueue<E> extends Queue<E> {

	@CheckForNull
	E dequeue();

	default E poll() {
		return dequeue();
	}

	default boolean pollAndApply(@Nonnull PollFunction<E> function) {
		return function.apply(dequeue());
	}

}
