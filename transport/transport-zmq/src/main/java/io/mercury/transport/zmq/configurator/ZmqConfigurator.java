package io.mercury.transport.zmq.configurator;

import io.mercury.transport.core.configurator.TransportConfigurator;

public final class ZmqConfigurator implements TransportConfigurator {

	private String host;
	private int port;
	private String topic;
	private int ioThreads;

	private final String fullInfo = "ZmqConfigurator";
	private final String connectionInfo;

	private ZmqConfigurator(Builder builder) {
		this.host = builder.host;
		this.port = builder.port;
		this.topic = builder.topic;
		this.ioThreads = builder.ioThreads;
		this.connectionInfo = host + ":" + port + "/" + topic;
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

	public String topic() {
		return topic;
	}

	public int ioThreads() {
		return ioThreads;
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

		private String host;
		private int port;
		private String topic;
		private int ioThreads;

		private Builder() {
		}

		public Builder setHost(String host) {
			this.host = host;
			return this;
		}

		public Builder setPort(int port) {
			this.port = port;
			return this;
		}

		public Builder setTopic(String topic) {
			this.topic = topic;
			return this;
		}

		public Builder setIoThreads(int ioThreads) {
			this.ioThreads = ioThreads;
			return this;
		}

		public ZmqConfigurator build() {
			return new ZmqConfigurator(this);
		}

	}
}