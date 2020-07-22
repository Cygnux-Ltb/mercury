package io.mercury.persistence.chronicle.exception;

public final class ChronicleWriteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3834475045588301175L;

	public ChronicleWriteException(Throwable throwable) {
		super(throwable.getMessage(), throwable);
	}

	public ChronicleWriteException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
