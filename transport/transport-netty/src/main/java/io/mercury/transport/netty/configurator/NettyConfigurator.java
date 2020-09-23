package io.mercury.transport.netty.configurator;

import java.util.concurrent.TimeUnit;

import io.mercury.common.functional.ShutdownEvent;
import io.mercury.transport.core.configurator.TransportConfigurator;

public class NettyConfigurator implements TransportConfigurator {

	private String host;
	private int port;
	private int backlog;
	private boolean keepAlive;
	private boolean tcpNoDelay;
	private long sendInterval;
	private TimeUnit sendIntervalTimeUnit;
	private int writeByteBufSize;
	private char separator;

	private ShutdownEvent shutdownEvent;

	private final String fullInfo = "NettyConfigurator";
	private final String connectionInfo = "";

	private NettyConfigurator(Builder builder) {
		this.host = builder.host;
		this.port = builder.port;
		this.backlog = builder.backlog;
		this.keepAlive = builder.keepAlive;
		this.tcpNoDelay = builder.tcpNoDelay;
		this.sendInterval = builder.sendInterval;
		this.sendIntervalTimeUnit = builder.sendIntervalTimeUnit;
		this.writeByteBufSize = builder.writeByteBufSize;
		this.separator = builder.separator;
		this.shutdownEvent = builder.shutdownEvent;
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

	public int backlog() {
		return backlog;
	}

	public boolean keepAlive() {
		return keepAlive;
	}

	public boolean tcpNoDelay() {
		return tcpNoDelay;
	}

	public long sendInterval() {
		return sendInterval;
	}

	public TimeUnit sendIntervalTimeUnit() {
		return sendIntervalTimeUnit;
	}

	public int writeByteBufSize() {
		return writeByteBufSize;
	}

	public char separator() {
		return separator;
	}

	public ShutdownEvent shutdownEvent() {
		return shutdownEvent;
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
		private int port = 9500;
		private int backlog = 128;
		private boolean keepAlive = true;
		private boolean tcpNoDelay = true;
		private long sendInterval;
		private TimeUnit sendIntervalTimeUnit;
		private int writeByteBufSize = 1024 * 8;
		private char separator = ';';
		private ShutdownEvent shutdownEvent;

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

		public Builder backlog(int backlog) {
			this.backlog = backlog;
			return this;
		}

		public Builder keepAlive(boolean keepAlive) {
			this.keepAlive = keepAlive;
			return this;
		}

		public Builder tcpNoDelay(boolean tcpNoDelay) {
			this.tcpNoDelay = tcpNoDelay;
			return this;
		}

		public Builder sendInterval(long sendInterval, TimeUnit sendIntervalTimeUnit) {
			this.sendInterval = sendInterval;
			this.sendIntervalTimeUnit = sendIntervalTimeUnit;
			return this;
		}

		public Builder writeByteBufSize(int writeByteBufSize) {
			this.writeByteBufSize = writeByteBufSize;
			return this;
		}

		public Builder separator(char separator) {
			this.separator = separator;
			return this;
		}

		public Builder shutdownEvent(ShutdownEvent shutdownEvent) {
			this.shutdownEvent = shutdownEvent;
			return this;
		}

		public NettyConfigurator build() {
			return new NettyConfigurator(this);
		}

	}

}
