package io.mercury.transport.core.api;

public interface Transport {

	String getName();

	boolean isConnected();

	boolean destroy();

}
