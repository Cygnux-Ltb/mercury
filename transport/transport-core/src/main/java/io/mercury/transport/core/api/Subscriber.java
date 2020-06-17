package io.mercury.transport.core.api;

import io.mercury.transport.core.TransportModule;

public interface Subscriber extends TransportModule{

	void subscribe();
	
	void reconnect();

}
