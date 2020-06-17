package io.mercury.transport.socket.configurator;

import io.mercury.transport.core.configurator.TransportConfigurator;

public class SocketConfigurator implements TransportConfigurator {

	private String host;
	private int port;
	private long receiveInterval;
	private int sendQueueSize;

	private final String fullInfo = "SocketConfigurator";
	private final String connectionInfo = "";

	private SocketConfigurator(Builder builder) {
		this.host = builder.host;
		this.port = builder.port;
		this.receiveInterval = builder.receiveInterval;
		this.sendQueueSize = builder.sendQueueSize;
	}

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public String host() {
		return host;
	}

	@Override
	public int port() {
		return port;
	}

	public long receiveInterval() {
		return receiveInterval;
	}

	public int sendQueueSize() {
		return sendQueueSize;
	}

	@Override
	public String fullInfo() {
		return fullInfo;
	}
	
	@Override
	public String connectionInfo() {
		return connectionInfo;
	}

	public static class Builder {

		private String host = "127.0.0.1";
		private int port;
		private long receiveInterval = 100;
		private int sendQueueSize = 256;

		private Builder() {
		}

		public Builder host(String host) {
			this.host = host;
			return this;
		}

		public Builder port(int port) {
			this.port = port;
			return this;
		}

		public Builder receiveInterval(long receiveInterval) {
			this.receiveInterval = receiveInterval;
			return this;
		}

		public Builder sendQueueSize(int sendQueueSize) {
			this.sendQueueSize = sendQueueSize;
			return this;
		}

		public SocketConfigurator build() {
			return new SocketConfigurator(this);
		}

	}
}