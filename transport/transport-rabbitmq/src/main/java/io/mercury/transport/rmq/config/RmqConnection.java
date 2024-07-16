package io.mercury.transport.rmq.cfg;

import com.rabbitmq.client.ConnectionFactory;
import com.typesafe.config.Config;
import io.mercury.common.config.ConfigWrapper;
import io.mercury.common.lang.Asserter;
import io.mercury.common.util.StringSupport;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.TransportConfigurator;
import io.mercury.transport.rmq.RmqTransport.ShutdownSignalHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.net.ssl.SSLContext;

public final class RmqConnection implements TransportConfigurator {

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

    // SSL
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
    private final transient ShutdownSignalHandler shutdownSignalHandler;

    // 配置连接信息
    @Getter
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
        this.shutdownSignalHandler = builder.shutdownSignalHandler;
        this.connectionInfo = username + "@" + host + ":" + port
                + (virtualHost.equals("/") ? virtualHost : "/" + virtualHost);
    }

    /**
     * @param host     String
     * @param port     int
     * @param username String
     * @param password String
     * @return Builder
     */
    public static Builder with(@Nonnull String host, int port,
                               @Nonnull String username, @Nonnull String password) {
        return new Builder(host, port, username, password);
    }

    /**
     * @param host        String
     * @param port        int
     * @param username    String
     * @param password    String
     * @param virtualHost String
     * @return Builder
     */
    public static Builder with(@Nonnull String host, int port,
                               @Nonnull String username, @Nonnull String password,
                               @CheckForNull String virtualHost) {
        return new Builder(host, port, username, password, virtualHost);
    }

    /**
     * @param config Config
     * @return Builder
     */
    public static Builder with(@Nonnull Config config) {
        return with("", config);
    }

    /**
     * @param module String
     * @param config Config
     * @return Builder
     */
    public static Builder with(@Nonnull String module, @Nonnull Config config) {
        ConfigWrapper<RmqCfgOption> wrapper = new ConfigWrapper<>(module, config);
        return new Builder(wrapper.getString(RmqCfgOption.Host),
                wrapper.getInt(RmqCfgOption.Port),
                wrapper.getString(RmqCfgOption.Username),
                wrapper.getString(RmqCfgOption.Password));
    }

    @Override
    public String getConfigInfo() {
        return toString();
    }

    private transient String toStringCache;

    @Override
    public String toString() {
        if (toStringCache == null)
            toStringCache = JsonWrapper.toJsonHasNulls(this);
        return toStringCache;
    }

    /**
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

    @Setter
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
            Asserter.nonNull(host, "host");
            Asserter.atWithinRange(port, 4096, 65536, "port");
            Asserter.nonNull(username, "username");
            Asserter.nonNull(password, "password");
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
            if (StringSupport.nonEmpty(virtualHost) && !virtualHost.equals("/"))
                this.virtualHost = virtualHost;
        }

        public RmqConnection build() {
            return new RmqConnection(this);
        }

    }

    public static void main(String[] args) {

        RmqConnection configuration = with("localhost", 5672, "admin", "admin", "report").build();
        System.out.println(configuration);
        System.out.println(configuration.getConfigInfo());

    }

}
