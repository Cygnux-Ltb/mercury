package io.mercury.persistence.chronicle.exception;

import io.mercury.common.character.Separator;

import java.io.Serial;

public final class ChronicleIOException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 439989412345254532L;

	public ChronicleIOException(Throwable cause) {
		super(cause);
	}

	public ChronicleIOException(String message, Throwable cause) {
		super(message + Separator.LINE_SEPARATOR + "because : " + cause.getMessage(), cause);
	}

}
