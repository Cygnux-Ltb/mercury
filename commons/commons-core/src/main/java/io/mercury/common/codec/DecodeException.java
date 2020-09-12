package io.mercury.common.codec;

/**
 * 
 * @author yellow013
 *
 */
public final class DecodeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1498435101978546958L;

	public DecodeException(String msg) {
		super(msg);
	}

	public DecodeException(Throwable cause) {
		super(cause);
	}

	public DecodeException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
