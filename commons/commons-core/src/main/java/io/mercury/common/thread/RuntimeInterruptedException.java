package io.mercury.common.thread;

public class RuntimeInterruptedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4687520758683166486L;

	public RuntimeInterruptedException(String message, InterruptedException e) {
		super(message, e);
	}

	public RuntimeInterruptedException(InterruptedException e) {
		super("Catch [InterruptedException] and conversion to [RuntimeInterruptedException]\nmessage : "
				+ e.getMessage(), e);
	}

}
