package io.mercury.common.lang.exception;

public final class ComponentStartupException extends RuntimeException {

	private static final long serialVersionUID = -5641797236046682009L;

	public ComponentStartupException(String msg) {
		super(msg);
	}

	public ComponentStartupException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
