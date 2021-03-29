package io.mercury.transport.core.api;

public interface TransportModule {

	String getName();

	boolean isConnected();

	boolean destroy();

}
