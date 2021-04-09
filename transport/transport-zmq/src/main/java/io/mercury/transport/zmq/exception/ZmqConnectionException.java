package io.mercury.transport.zmq.exception;

import io.mercury.transport.zmq.cfg.ZmqAddress;

public final class ZmqConnectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6368403482174464676L;

	public ZmqConnectionException(ZmqAddress addr) {
		super("Unable to connect : " + addr.getAddr());
	}

}
