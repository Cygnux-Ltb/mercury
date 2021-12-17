package io.mercury.transport.rabbitmq.configurator;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.net.ssl.SSLContext;

import com.rabbitmq.client.ConnectionFactory;

import io.mercury.common.lang.Assertor;
import io.mercury.common.util.StringSupport;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.configurator.TransportConfigurator;
import io.mercury.transport.rabbitmq.RabbitMqTransport.ShutdownSignalHandler;

public final class RabbitConnection implements TransportConfigurator {

	// 连接地址
	private final String host;

	// 端口号
	private final int port;

	// 用户名
	private final String username;

	// 密码
	private final String password;

	// 虚拟主机
	private final String virtualHost;

	// SSL
	private final SSLContext sslContext;

	// 连接超时时间
	private final int connectionTimeout;

	// 自动恢复连接
	private final boolean automaticRecovery;

	// 重试连接间隔
	private final long recoveryInterval;

	// 握手通信超时时间
	private final int handshakeTimeout;

	// 关闭超时时间
	private final int shutdownTimeout;

	// 请求心跳超时时间
	private final int requestedHeartbeat;

	// 停机处理回调函数
	private final transient ShutdownSignalHandler shutdownSignalHandler;

	// 配置连接信息
	private final String connectionInfo;

	private RabbitConnection(Builder builder) {
		this.host = builder.host;
		this.port = builder.port;
		this.username = builder.username;
		this.password = builder.password;
		this.virtualHost = builder.virtualHost;
		this.sslContext = builder.sslContext;
		this.connectionTimeout = builder.connectionTimeout;
		this.automaticRecovery = builder.automaticRecovery;
		this.recoveryInterval = builder.recoveryInterval;
		this.handshakeTimeout = builder.handshakeTimeout;
		this.shutdownTimeout = builder.shutdownTimeout;
		this.requestedHeartbeat = builder.requestedHeartbeat;
		this.shutdownSignalHandler = builder.shutdownSignalHandler;
		this.connectionInfo = username + "@" + host + ":" + port
				+ (virtualHost.equals("/") ? virtualHost : "/" + virtualHost);
	}

	/**
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
	public static Builder configuration(@Nonnull String host, int port, @Nonnull String username,
			@Nonnull String password) {
		return new Builder(host, port, username, password);
	}

	/**
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param virtualHost
	 * @return
	 */
	public static Builder configuration(@Nonnull String host, int port, @Nonnull String username,
			@Nonnull String password, @CheckForNull String virtualHost) {
		return new Builder(host, port, username, password, virtualHost);
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public SSLContext getSslContext() {
		return sslContext;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public boolean isAutomaticRecovery() {
		return automaticRecovery;
	}

	public long getRecoveryInterval() {
		return recoveryInterval;
	}

	public int getHandshakeTimeout() {
		return handshakeTimeout;
	}

	public int getShutdownTimeout() {
		return shutdownTimeout;
	}

	public int getRequestedHeartbeat() {
		return requestedHeartbeat;
	}

	public ShutdownSignalHandler getShutdownSignalHandler() {
		return shutdownSignalHandler;
	}

	@Override
	public String getConnectionInfo() {
		return connectionInfo;
	}

	@Override
	public String getConfigInfo() {
		return toString();
	}

	private transient String cache;

	@Override
	public String toString() {
		if (cache == null)
			cache = JsonWrapper.toJsonHasNulls(this);
		return cache;
	}

	/**
	 * 
	 * @return ConnectionFactory
	 */
	public ConnectionFactory createConnectionFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setVirtualHost(virtualHost);
		factory.setAutomaticRecoveryEnabled(automaticRecovery);
		factory.setNetworkRecoveryInterval(recoveryInterval);
		factory.setHandshakeTimeout(handshakeTimeout);
		factory.setConnectionTimeout(connectionTimeout);
		factory.setShutdownTimeout(shutdownTimeout);
		factory.setRequestedHeartbeat(requestedHeartbeat);
		if (sslContext != null)
			factory.useSslProtocol(sslContext);
		return factory;
	}

	public static class Builder {
		// 连接地址
		private final String host;
		// 端口号
		private final int port;
		// 用户名
		private final String username;
		// 密码
		private final String password;
		// 虚拟主机
		private String virtualHost = "/";

		// SSL上下文
		private SSLContext sslContext;

		// 连接超时时间
		private int connectionTimeout = 60 * 1000;

		// 自动恢复连接
		private boolean automaticRecovery = true;

		// 重试连接间隔(毫秒)
		private long recoveryInterval = 10 * 1000;

		// 握手通信超时时间(毫秒)
		private int handshakeTimeout = 10 * 1000;

		// 关闭超时时间(毫秒)
		private int shutdownTimeout = 10 * 1000;

		// 请求心跳超时时间(秒)
		private int requestedHeartbeat = 20;

		// 停机处理回调函数
		private ShutdownSignalHandler shutdownSignalHandler;

		private Builder(String host, int port, String username, String password) {
			this(host, port, username, password, "/");
		}

		private Builder(String host, int port, String username, String password, String virtualHost) {
			Assertor.nonNull(host, "host");
			Assertor.atWithinRange(port, 4096, 65536, "port");
			Assertor.nonNull(username, "username");
			Assertor.nonNull(password, "password");
			this.host = host;
			this.port = port;
			this.username = username;
			this.password = password;
			if (StringSupport.nonEmpty(virtualHost) && !virtualHost.equals("/"))
				this.virtualHost = virtualHost;
		}

		public Builder setSslContext(SSLContext sslContext) {
			this.sslContext = sslContext;
			return this;
		}

		public Builder setConnectionTimeout(int connectionTimeout) {
			this.connectionTimeout = connectionTimeout;
			return this;
		}

		public Builder setAutomaticRecovery(boolean automaticRecovery) {
			this.automaticRecovery = automaticRecovery;
			return this;
		}

		public Builder setRecoveryInterval(long recoveryInterval) {
			this.recoveryInterval = recoveryInterval;
			return this;
		}

		public Builder setHandshakeTimeout(int handshakeTimeout) {
			this.handshakeTimeout = handshakeTimeout;
			return this;
		}

		public Builder setShutdownTimeout(int shutdownTimeout) {
			this.shutdownTimeout = shutdownTimeout;
			return this;
		}

		public Builder setRequestedHeartbeat(int requestedHeartbeat) {
			this.requestedHeartbeat = requestedHeartbeat;
			return this;
		}

		public Builder setShutdownSignalHandler(ShutdownSignalHandler shutdownSignalHandler) {
			this.shutdownSignalHandler = shutdownSignalHandler;
			return this;
		}

		public RabbitConnection build() {
			return new RabbitConnection(this);
		}

	}

	public static void main(String[] args) {

		RabbitConnection configuration = configuration("localhost", 5672, "admin", "admin", "report").build();
		System.out.println(configuration);
		System.out.println(configuration.getConfigInfo());

	}

}
