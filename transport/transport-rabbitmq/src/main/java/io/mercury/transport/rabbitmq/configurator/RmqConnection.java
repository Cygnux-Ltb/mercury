package io.mercury.transport.rabbitmq.configurator;

import static io.mercury.common.util.Assertor.nonNull;
import static io.mercury.common.util.Assertor.withinRange;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.net.ssl.SSLContext;

import com.rabbitmq.client.ConnectionFactory;

import io.mercury.common.functional.ShutdownEvent;
import io.mercury.common.util.StringUtil;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.core.configurator.TransportConfigurator;

public final class RmqConnection implements TransportConfigurator {

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

	// SSL上下文
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
	private final transient ShutdownEvent shutdownEvent;

	// 配置连接信息
	private final String connectionInfo;

	private RmqConnection(Builder builder) {
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
		this.shutdownEvent = builder.shutdownEvent;
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
			@Nonnull String password, String virtualHost) {
		return new Builder(host, port, username, password, virtualHost);
	}

	@Override
	public String connectionInfo() {
		return connectionInfo;
	}

	@Override
	public String host() {
		return host;
	}

	@Override
	public int port() {
		return port;
	}

	/**
	 * @return the username
	 */
	public String username() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String password() {
		return password;
	}

	/**
	 * @return the virtualHost
	 */
	public String virtualHost() {
		return virtualHost;
	}

	/**
	 * @return the sslContext
	 */
	public SSLContext sslContext() {
		return sslContext;
	}

	/**
	 * @return the connectionTimeout
	 */
	public int connectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * @return the automaticRecovery
	 */
	public boolean automaticRecovery() {
		return automaticRecovery;
	}

	/**
	 * @return the recoveryInterval
	 */
	public long recoveryInterval() {
		return recoveryInterval;
	}

	/**
	 * @return the handshakeTimeout
	 */
	public int handshakeTimeout() {
		return handshakeTimeout;
	}

	/**
	 * @return the shutdownTimeout
	 */
	public int shutdownTimeout() {
		return shutdownTimeout;
	}

	/**
	 * @return the requestedHeartbeat
	 */
	public int requestedHeartbeat() {
		return requestedHeartbeat;
	}

	/**
	 * @return the shutdownEvent
	 */
	public ShutdownEvent shutdownEvent() {
		return shutdownEvent;
	}

	/**
	 * 
	 */
	@Override
	public String fullInfo() {
		return toString();
	}

	private transient String toStringCache;

	@Override
	public String toString() {
		if (toStringCache == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("connection", this);
			toStringCache = JsonWrapper.toJson(map);
			// StringUtil.toStringForReflection(this);
		}
		return toStringCache;
	}

	/**
	 * 
	 * @return ConnectionFactory
	 */
	public ConnectionFactory newConnectionFactory() {
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
		private ShutdownEvent shutdownEvent;

		private Builder(String host, int port, String username, String password) {
			this(host, port, username, password, "/");
		}

		private Builder(String host, int port, String username, String password, String virtualHost) {
			nonNull(host, "host");
			withinRange(port, 4096, 65536, "port");
			nonNull(username, "username");
			nonNull(password, "password");
			this.host = host;
			this.port = port;
			this.username = username;
			this.password = password;
			if (StringUtil.nonEmpty(virtualHost) && !virtualHost.equals("/"))
				this.virtualHost = virtualHost;
		}

		public RmqConnection build() {
			return new RmqConnection(this);
		}

		/**
		 * @param sslContext the sslContext to set
		 */
		public Builder setSslContext(SSLContext sslContext) {
			this.sslContext = sslContext;
			return this;
		}

		/**
		 * @param connectionTimeout the connectionTimeout to set
		 */
		public Builder setConnectionTimeout(int connectionTimeout) {
			this.connectionTimeout = connectionTimeout;
			return this;
		}

		/**
		 * @param automaticRecovery the automaticRecovery to set
		 */
		public Builder setAutomaticRecovery(boolean automaticRecovery) {
			this.automaticRecovery = automaticRecovery;
			return this;
		}

		/**
		 * @param recoveryInterval the recoveryInterval to set
		 */
		public Builder setRecoveryInterval(long recoveryInterval) {
			this.recoveryInterval = recoveryInterval;
			return this;
		}

		/**
		 * @param handshakeTimeout the handshakeTimeout to set
		 */
		public Builder setHandshakeTimeout(int handshakeTimeout) {
			this.handshakeTimeout = handshakeTimeout;
			return this;
		}

		/**
		 * @param requestedHeartbeat the requestedHeartbeat to set
		 */
		public Builder setRequestedHeartbeat(int requestedHeartbeat) {
			this.requestedHeartbeat = requestedHeartbeat;
			return this;
		}

		/**
		 * @param shutdownTimeout the shutdownTimeout to set
		 */
		public Builder setShutdownTimeout(int shutdownTimeout) {
			this.shutdownTimeout = shutdownTimeout;
			return this;
		}

		/**
		 * @param shutdownEvent the shutdownEvent to set
		 */
		public Builder setShutdownEvent(ShutdownEvent shutdownEvent) {
			this.shutdownEvent = shutdownEvent;
			return this;
		}

	}

	public static void main(String[] args) {

		RmqConnection configuration = configuration("localhost", 5672, "admin", "admin", "report").build();
		System.out.println(configuration);
		System.out.println(configuration.fullInfo());

	}

}
