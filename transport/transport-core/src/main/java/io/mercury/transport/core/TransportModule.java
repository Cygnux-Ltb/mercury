package io.mercury.transport.core;

public interface TransportModule {

	String name();

	boolean isConnected();

	boolean destroy();

}
