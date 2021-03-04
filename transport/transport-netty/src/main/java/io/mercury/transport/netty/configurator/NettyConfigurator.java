package io.mercury.transport.netty.configurator;

import java.util.concurrent.TimeUnit;

import io.mercury.common.functional.ShutdownEvent;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.core.configurator.TransportConfigurator;

public final class NettyConfigurator implements TransportConfigurator {

	private final String host;
	private final int port;
	private final int backlog;
	private final boolean keepAlive;
	private final boolean tcpNoDelay;
	private final long sendInterval;
	private final TimeUnit sendIntervalTimeUnit;
	private final int writeByteBufSize;
	private final char separator;

	/**
	 * 
	 */
	private final ShutdownEvent shutdownEvent;

	/**
	 * 
	 */
	private final String connectionInfo;

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
		this.connectionInfo = host + ":" + port;
	}

	public static Builder builder(String host, int port) {
		return new Builder(host, port);
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
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
	public String getConfiguratorInfo() {
		return toString();
	}

	@Override
	public String getConnectionInfo() {
		return connectionInfo;
	}

	private transient String toStringCache;

	@Override
	public String toString() {
		if (toStringCache == null)
			this.toStringCache = JsonWrapper.toJson(this);
		return toStringCache;
	}

	public static class Builder {

		private final String host;
		private final int port;
		private int backlog = 128;
		private boolean keepAlive = true;
		private boolean tcpNoDelay = true;
		private long sendInterval;
		private TimeUnit sendIntervalTimeUnit;
		private int writeByteBufSize = 1024 * 8;
		private char separator = ';';
		private ShutdownEvent shutdownEvent;

		private Builder(String host, int port) {
			super();
			this.host = host;
			this.port = port;
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
