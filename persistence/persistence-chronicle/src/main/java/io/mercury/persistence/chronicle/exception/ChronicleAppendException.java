package io.mercury.persistence.chronicle.exception;

public final class ChronicleAppendException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3834475045588301175L;

	public ChronicleAppendException(Throwable throwable) {
		super(throwable.getMessage(), throwable);
	}

	public ChronicleAppendException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
