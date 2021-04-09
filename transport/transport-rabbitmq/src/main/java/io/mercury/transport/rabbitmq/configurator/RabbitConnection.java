package io.mercury.transport.rabbitmq.configurator;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.net.ssl.SSLContext;

import com.rabbitmq.client.ConnectionFactory;

import io.mercury.common.functional.ShutdownEvent;
import io.mercury.common.util.Assertor;
import io.mercury.common.util.StringUtil;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.configurator.TransportConfigurator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public final class RabbitConnection implements TransportConfigurator {

	// 连接地址
	@Getter
	private final String host;

	// 端口号
	@Getter
	private final int port;

	// 用户名
	@Getter
	private final String username;

	// 密码
	@Getter
	private final String password;

	// 虚拟主机
	@Getter
	private final String virtualHost;

	// SSL上下文
	@Getter
	private final SSLContext sslContext;

	// 连接超时时间
	@Getter
	private final int connectionTimeout;

	// 自动恢复连接
	@Getter
	private final boolean automaticRecovery;

	// 重试连接间隔
	@Getter
	private final long recoveryInterval;

	// 握手通信超时时间
	@Getter
	private final int handshakeTimeout;

	// 关闭超时时间
	@Getter
	private final int shutdownTimeout;

	// 请求心跳超时时间
	@Getter
	private final int requestedHeartbeat;

	// 停机处理回调函数
	@Getter
	private final transient ShutdownEvent shutdownEvent;

	// 配置连接信息
	@Getter
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
			@Nonnull String password, @CheckForNull String virtualHost) {
		return new Builder(host, port, username, password, virtualHost);
	}

	/**
	 * 
	 */
	@Override
	public String getConfiguratorInfo() {
		return toString();
	}

	private transient String toStringCache;

	@Override
	public String toString() {
		if (toStringCache == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("connection", this);
			toStringCache = JsonWrapper.toJson(map);
		}
		return toStringCache;
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

	@Accessors(chain = true)
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
		@Setter
		private SSLContext sslContext;

		// 连接超时时间
		@Setter
		private int connectionTimeout = 60 * 1000;

		// 自动恢复连接
		@Setter
		private boolean automaticRecovery = true;

		// 重试连接间隔(毫秒)
		@Setter
		private long recoveryInterval = 10 * 1000;

		// 握手通信超时时间(毫秒)
		@Setter
		private int handshakeTimeout = 10 * 1000;

		// 关闭超时时间(毫秒)
		@Setter
		private int shutdownTimeout = 10 * 1000;

		// 请求心跳超时时间(秒)
		@Setter
		private int requestedHeartbeat = 20;

		// 停机处理回调函数
		@Setter
		private ShutdownEvent shutdownEvent;

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
			if (StringUtil.nonEmpty(virtualHost) && !virtualHost.equals("/"))
				this.virtualHost = virtualHost;
		}

		public RabbitConnection build() {
			return new RabbitConnection(this);
		}

	}

	public static void main(String[] args) {

		RabbitConnection configuration = configuration("localhost", 5672, "admin", "admin", "report").build();
		System.out.println(configuration);
		System.out.println(configuration.getConfiguratorInfo());

	}

}
