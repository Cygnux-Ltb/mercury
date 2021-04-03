package io.mercury.transport.api;

public interface Transport {

	String getName();

	boolean isConnected();

	boolean destroy();

}
