package com.linkedin.avro.fastserde;

public class SchemaAssistantException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6322376693674783661L;

	public SchemaAssistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public SchemaAssistantException(String message) {
		super(message);
	}

	public SchemaAssistantException(Throwable cause) {
		super(cause);
	}
}
