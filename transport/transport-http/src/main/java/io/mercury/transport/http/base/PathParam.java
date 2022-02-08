package io.mercury.transport.http.base;

public class PathParam {

	private final String param;
	private final Object value;

	/**
	 * @param name
	 * @param value
	 */
	public PathParam(String param, Object value) {
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