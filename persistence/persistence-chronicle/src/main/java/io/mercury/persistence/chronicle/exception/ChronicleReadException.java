package io.mercury.persistence.chronicle.exception;

public class ChronicleReadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3834475045588301175L;

	public ChronicleReadException(Throwable throwable) {
		super(throwable);
	}

	public ChronicleReadException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
