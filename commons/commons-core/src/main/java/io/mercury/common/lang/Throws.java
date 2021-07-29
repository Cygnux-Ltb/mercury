package io.mercury.common.lang;

public final class Throws {

	/**
	 * 
	 * @throws IllegalArgumentException
	 */
	public static final void illegalArgument() throws IllegalArgumentException {
		throw new IllegalArgumentException();
	}

	/**
	 * 
	 * @param msg
	 * @throws IllegalArgumentException
	 */
	public static final void illegalArgument(String msg) throws IllegalArgumentException {
		throw new IllegalArgumentException("illegal argument -> " + msg);
	}

}
