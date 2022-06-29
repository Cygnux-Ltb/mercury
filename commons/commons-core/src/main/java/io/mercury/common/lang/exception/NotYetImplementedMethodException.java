package io.mercury.common.lang.exception;

import java.io.Serial;

public final class NotYetImplementedMethodException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 947213334800336824L;

	public NotYetImplementedMethodException(String msg) {
		super(msg);
	}

}
