package io.mercury.common.codec;

public final class EncodeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6170535313072988508L;

	public EncodeException(String msg) {
		super(msg);
	}

	public EncodeException(Throwable cause) {
		super(cause);
	}

	public EncodeException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
