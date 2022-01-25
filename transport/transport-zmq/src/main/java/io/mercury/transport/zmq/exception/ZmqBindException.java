package io.mercury.transport.zmq.exception;

public final class ZmqBindException extends RuntimeException {

	private static final long serialVersionUID = -4110788670578471831L;

	public ZmqBindException(String addr) {
		super("Unable to bind -> " + addr);
	}

}
