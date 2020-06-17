package io.mercury.transport.core.api;

import io.mercury.transport.core.TransportModule;

public interface Sender<T> extends TransportModule {

	void send(T msg);

}
