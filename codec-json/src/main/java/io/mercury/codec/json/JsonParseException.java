package io.mercury.codec.json;

public final class JsonParseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9000408863460789219L;

	public JsonParseException(String json, Throwable throwable) {
		super("Parsing json [" + json + "] throw exception [" + throwable.getClass().getSimpleName() + "]", throwable);
	}

	public JsonParseException(Throwable throwable) {
		super("Parsing json throw exception [" + throwable.getClass().getSimpleName() + "]", throwable);
	}

	public JsonParseException(String message) {
		super(message);
	}

}
