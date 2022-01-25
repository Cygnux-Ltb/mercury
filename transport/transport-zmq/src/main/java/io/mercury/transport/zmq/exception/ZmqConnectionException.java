package io.mercury.transport.zmq.exception;

public final class ZmqConnectionException extends RuntimeException {

	private static final long serialVersionUID = -6368403482174464676L;

	public ZmqConnectionException(String addr) {
		super("Unable to connect -> " + addr);
	}

}
