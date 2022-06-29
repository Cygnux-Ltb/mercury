package io.mercury.common.lang.exception;

import java.io.Serial;

public final class NativeLibraryLoadException extends Exception {

	@Serial
	private static final long serialVersionUID = -5877471358569299269L;

	public NativeLibraryLoadException(String msg, Throwable cause) {
		super(msg, cause);
	}

}