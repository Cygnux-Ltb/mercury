package io.mercury.persistence.rocksdb.exception;

import java.io.Serial;

public final class RocksRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -6940482392634006619L;

	public RocksRuntimeException(String message) {
		super(message);
	}

	public RocksRuntimeException(Throwable throwable) {
		super(throwable);
	}

	public RocksRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
