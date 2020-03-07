package io.mercury.codec.json;

public final class JsonParseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9000408863460789219L;

	public JsonParseException(String json, Throwable throwable) {
		super("Parse JSON -> [" + json + "], Throw exception -> [" + throwable.getClass().getSimpleName() + "]",
				throwable);
	}

	public JsonParseException(Throwable throwable) {
		super("Parse JSON throw exception -> [" + throwable.getClass().getSimpleName() + "]", throwable);
	}

	public JsonParseException(String message) {
		super(message);
	}

}
