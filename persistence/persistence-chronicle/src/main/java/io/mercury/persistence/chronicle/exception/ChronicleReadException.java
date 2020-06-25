package io.mercury.persistence.chronicle.exception;

public final class ChronicleReadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7300071826810963772L;

	public ChronicleReadException(Throwable throwable) {
		super(throwable);
	}

	public ChronicleReadException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
