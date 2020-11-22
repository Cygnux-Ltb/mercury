package io.mercury.common.concurrent.queue.api;

import javax.annotation.CheckForNull;

public interface MCQueue<E> extends Queue<E> {

	@CheckForNull
	E dequeue();

	@Override
	default E poll() {
		return dequeue();
	}

	@Override
	default boolean pollAndApply(PollFunction<E> function) {
		return function.apply(dequeue());
	}

}
