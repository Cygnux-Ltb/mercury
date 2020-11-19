package io.mercury.transport.core.api;

public interface TransportModule {

	String name();

	boolean isConnected();

	boolean destroy();

}
