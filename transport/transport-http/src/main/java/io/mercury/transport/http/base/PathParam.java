package io.mercury.transport.http.base;

public final class PathParam {

	private final String param;
	private final String value;

	/**
	 * @param name
	 * @param value
	 */
	public PathParam(String param, String value) {
		this.param = param;
		this.value = value;
	}

	public String getParam() {
		return param;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return param + "=" + value;
	}

}