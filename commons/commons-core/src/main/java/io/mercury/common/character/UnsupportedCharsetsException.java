package io.mercury.common.character;

public final class UnsupportedCharsetsException extends RuntimeException {

	private static final long serialVersionUID = 499393971909495906L;

	public UnsupportedCharsetsException(String charsetName) {
		super("Cannot find charset : [" + charsetName + "]");
	}

}
