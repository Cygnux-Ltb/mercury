package io.mercury.transport.api;

import io.mercury.transport.exception.PublishFailedException;

public interface Proxy<T> extends Transport, Receiver, Publisher<T> {

	@Override
	default void publish(T msg) throws PublishFailedException {
		getUpstream().publish(msg);
	}

	@Override
	default void publish(String target, T msg) throws PublishFailedException {
		getUpstream().publish(target, msg);
	}

	Publisher<T> getUpstream();

	@Override
	default void run() {
		receive();
	}

}
