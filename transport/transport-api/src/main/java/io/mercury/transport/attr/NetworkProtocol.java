package io.mercury.transport.attr;

public enum NetworkProtocol {

	TCP("tcp://"),

	UDP("udp://"),

	HTTP("http://"),

	HTTPS("https://"),

	WS("ws://"),

	WSS("wss://"),

	;

	private final String prefix;

	private NetworkProtocol(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

}
