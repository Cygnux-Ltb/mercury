package io.mercury.persistence.chronicle.exception;

public final class ChronicleIOException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 439989412345254532L;

	public ChronicleIOException(Throwable throwable) {
		super(throwable.getMessage(), throwable);
	}

	public ChronicleIOException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
