package com.linkedin.avro.fastserde;

public class FastSerdeGeneratorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1359690878765595079L;

	public FastSerdeGeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

	public FastSerdeGeneratorException(String message) {
		super(message);
	}

	public FastSerdeGeneratorException(Throwable cause) {
		super(cause);
	}
}
