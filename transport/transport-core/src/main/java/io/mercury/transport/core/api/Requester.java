package io.mercury.transport.core.api;

import io.mercury.transport.core.TransportModule;

public interface Requester<T> extends TransportModule {

	T request();

}
