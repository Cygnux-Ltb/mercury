package io.mercury.transport.zmq;

import com.typesafe.config.Config;
import io.mercury.common.annotation.OnlyOverrideEquals;
import io.mercury.common.config.ConfigWrapper;
import io.mercury.common.lang.Asserter;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.net.IpAddressIllegalException;
import io.mercury.common.net.IpAddressValidator;
import io.mercury.common.serialization.specific.BytesSerializer;
import io.mercury.common.serialization.specific.JsonDeserializable;
import io.mercury.common.serialization.specific.JsonSerializable;
import io.mercury.common.util.StringSupport;
import io.mercury.serialization.json.JsonParser;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.TransportConfigurator;
import io.mercury.transport.attr.TcpKeepAlive;
import io.mercury.transport.attr.Topics;
import org.slf4j.Logger;
import org.zeromq.ZMQ;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static io.mercury.common.sys.CurrentRuntime.availableProcessors;
import static io.mercury.transport.zmq.ZmqConfigOption.Addr;
import static io.mercury.transport.zmq.ZmqConfigOption.IoThreads;
import static io.mercury.transport.zmq.ZmqConfigOption.Port;
import static io.mercury.transport.zmq.ZmqConfigOption.Protocol;

@OnlyOverrideEquals
public final class ZmqConfigurator implements
        TransportConfigurator, JsonSerializable, JsonDeserializable<ZmqConfigurator> {

    private static final Logger log = Log4j2LoggerFactory.getLogger(ZmqConfigurator.class);

    private final ZmqAddr addr;

    private int ioThreads = 1;

    private int highWaterMark = 8192;

    private TcpKeepAlive tcpKeepAlive = null;

    /**
     * @param addr ZmqAddr
     */
    private ZmqConfigurator(ZmqAddr addr) {
        this.addr = addr;
    }

    private ZmqConfigurator(ZmqProtocol protocol, String addr) {
        this(new ZmqAddr(protocol, addr));
    }

    public ZmqAddr getAddr() {
        return addr;
    }

    public int getIoThreads() {
        return ioThreads;
    }

    public int getHighWaterMark() {
        return highWaterMark;
    }

    public TcpKeepAlive getTcpKeepAlive() {
        return tcpKeepAlive;
    }

    @Override
    public String getConnectionInfo() {
        return addr.toString();
    }

    /**
     * @param config Config
     * @return ZmqConfigurator
     */
    public static ZmqConfigurator withConfig(@Nonnull Config config) {
        return withConfig("", config);
    }

    /**
     * @param config String
     * @param module Config
     * @return ZmqConfigurator
     */
    public static ZmqConfigurator withConfig(String module, @Nonnull Config config) {
        Asserter.nonNull(config, "config");
        var delegate = new ConfigWrapper<ZmqConfigOption>(module, config);
        var protocol = ZmqProtocol.of(delegate.getStringOrThrows(Protocol));
        ZmqConfigurator conf;
        switch (protocol) {
            // 使用tcp协议
            case TCP -> {
                int port = delegate.getIntOrThrows(Port);
                if (delegate.hasOption(Addr)) {
                    var tcpAddr = delegate.getStringOrThrows(Addr);
                    Asserter.isValid(tcpAddr, IpAddressValidator::isIpAddress,
                            new IpAddressIllegalException(tcpAddr));
                    conf = ZmqConfigurator.tcp(tcpAddr, port);
                } else {
                    // 没有addr配置项, 使用本地地址
                    conf = ZmqConfigurator.tcp(port);
                }
            }
            // 使用ipc或inproc协议
            case IPC, INPROC -> {
                var localAddr = delegate.getStringOrThrows(Addr);
                conf = new ZmqConfigurator(protocol, localAddr);
            }
            default -> throw new UnsupportedOperationException(StringSupport.toString(protocol));
        }
        if (delegate.hasOption(IoThreads))
            conf.ioThreads(delegate.getInt(IoThreads));
        log.info("created ZmqConfigurator object -> {}", conf);
        return conf;
    }

    /**
     * 创建TCP协议连接
     *
     * @param port int
     * @return ZmqConfigurator
     */
    public static ZmqConfigurator tcp(int port) {
        return tcp("*", port);
    }

    /**
     * 创建TCP协议连接
     *
     * @param addr String
     * @param port int
     * @return ZmqConfigurator
     */
    public static ZmqConfigurator tcp(@Nonnull String addr, int port) {
        return new ZmqConfigurator(ZmqAddr.tcp(addr, port));
    }

    /**
     * 使用[ipc]协议连接, 用于进程间通信
     *
     * @param addr ZmqConfigurator
     * @return String
     */
    public static ZmqConfigurator ipc(@Nonnull String addr) {
        return new ZmqConfigurator(ZmqAddr.ipc(addr));
    }

    /**
     * 使用[inproc]协议连接, 用于线程间通信
     *
     * @param addr String
     * @return ZmqConfigurator
     */
    public static ZmqConfigurator inproc(@Nonnull String addr) {
        return new ZmqConfigurator(ZmqAddr.inproc(addr));
    }

    /**
     * @param ioThreads int
     * @return ZmqConfigurator
     */
    public ZmqConfigurator ioThreads(int ioThreads) {
        Asserter.greaterThan(ioThreads, 0, "ioThreads");
        this.ioThreads = Math.min(ioThreads, availableProcessors());
        return this;
    }

    /**
     * Set high watermark with socket
     *
     * @param highWaterMark int
     * @return ZmqConfigurator
     */
    public ZmqConfigurator setHighWaterMark(int highWaterMark) {
        this.highWaterMark = highWaterMark;
        return this;
    }

    /**
     * @param tcpKeepAlive TcpKeepAlive
     * @return ZmqConfigurator
     */
    public ZmqConfigurator tcpKeepAlive(@Nonnull TcpKeepAlive tcpKeepAlive) {
        Asserter.nonNull(tcpKeepAlive, "tcpKeepAlive");
        this.tcpKeepAlive = tcpKeepAlive;
        return this;
    }

    /**
     * @return ZmqSender<byte [ ]>
     */
    public ZmqSender<byte[]> newSender() {
        return newSender(bytes -> bytes);
    }

    /**
     * @param <T> T type
     * @param ser BytesSerializer<T>
     * @return ZmqSender<T>
     */
    public <T> ZmqSender<T> newSender(@Nonnull BytesSerializer<T> ser) {
        Asserter.nonNull(ser, "ser");
        return new ZmqSender<>(this, ser);
    }

    /**
     * @param handler Function<byte[], byte[]>
     * @return ZmqReceiver
     */
    public ZmqReceiver newReceiver(@Nonnull Function<byte[], byte[]> handler) {
        Asserter.nonNull(handler, "handler");
        return new ZmqReceiver(this, handler);
    }

    /**
     * @param consumer BiConsumer<byte[], byte[]>
     * @return ZmqSubscriber
     */
    public ZmqSubscriber newSubscriber(@Nonnull BiConsumer<byte[], byte[]> consumer) {
        return newSubscriber(Topics.with(""), consumer);
    }

    /**
     * @param topics   Topics
     * @param consumer BiConsumer<byte[], byte[]>
     * @return ZmqSubscriber
     */
    public ZmqSubscriber newSubscriber(@Nonnull Topics topics, @Nonnull BiConsumer<byte[], byte[]> consumer) {
        Asserter.nonNull(topics, "topics");
        Asserter.nonNull(consumer, "consumer");
        return new ZmqSubscriber(this, topics, consumer);
    }

    /**
     * @return ZmqPublisher
     */
    public ZmqPublisher<byte[]> newPublisherWithBinary() {
        return newPublisherWithBinary("");
    }

    /**
     * @param topic String
     * @return ZmqPublisher
     */
    public ZmqPublisher<byte[]> newPublisherWithBinary(@Nonnull String topic) {
        return newPublisher(topic, bytes -> bytes);
    }

    /**
     * @return ZmqPublisher
     */
    public ZmqPublisher<String> newPublisherWithString() {
        return newPublisherWithString("", ZMQ.CHARSET);
    }

    /**
     * @param topic String
     * @return ZmqPublisher
     */
    public ZmqPublisher<String> newPublisherWithString(@Nonnull String topic) {
        return newPublisherWithString(topic, ZMQ.CHARSET);
    }

    /**
     * @param encode Charset
     * @return ZmqPublisher
     */
    public ZmqPublisher<String> newPublisherWithString(@Nonnull Charset encode) {
        return newPublisherWithString("", encode);
    }

    /**
     * @param topic  String
     * @param encode Charset
     * @return ZmqPublisher
     */
    public ZmqPublisher<String> newPublisherWithString(@Nonnull String topic, @Nonnull Charset encode) {
        Asserter.nonNull(encode, "encode");
        return newPublisher(topic, str -> str.getBytes(encode));
    }

    /**
     * @param <T> T type
     * @param ser BytesSerializer<T>
     * @return ZmqPublisher
     */
    public <T> ZmqPublisher<T> newPublisher(@Nonnull BytesSerializer<T> ser) {
        return newPublisher("", ser);
    }

    /**
     * @param <T>   T type
     * @param topic String
     * @param ser   BytesSerializer<T>
     * @return ZmqPublisher
     */
    public <T> ZmqPublisher<T> newPublisher(@Nonnull String topic, @Nonnull BytesSerializer<T> ser) {
        Asserter.nonNull(topic, "topic");
        Asserter.nonNull(ser, "ser");
        return new ZmqPublisher<>(this, topic, ser);
    }

    private transient String toString;

    @Override
    public String toString() {
        if (toString == null)
            this.toString = JsonWrapper.toJson(this);
        return toString;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof ZmqConfigurator o))
            return false;
        else {
            if (!this.addr.equals(o.getAddr()))
                return false;
            if (this.ioThreads != o.getIoThreads())
                return false;
            return this.tcpKeepAlive.equals(o.getTcpKeepAlive());
        }
    }

    @Override
    public String getConfigInfo() {
        return toString();
    }

    public static String getZmqVersion() {
        return ZMQ.getVersionString();
    }

    @Nonnull
    @Override
    public String toJson() {
        return JsonWrapper.toJsonHasNulls(this);
    }

    @Nonnull
    @Override
    public ZmqConfigurator fromJson(@Nonnull String json) {
        Map<String, Object> map = JsonParser.toMap(json);
        ZmqProtocol protocol = ZmqProtocol.of((String) map.get("protocol"));
        String addr = (String) map.get("addr");
        int ioThreads = (int) map.get("ioThreads");
        TcpKeepAlive tcpKeepAlive = JsonParser.toObject((String) map.get("tcpKeepAlive"), TcpKeepAlive.class);
        assert tcpKeepAlive != null;
        return new ZmqConfigurator(protocol, addr).ioThreads(ioThreads).tcpKeepAlive(tcpKeepAlive);
    }

    /**
     * 当前支持的协议类型
     *
     * @author yellow013
     */
    public enum ZmqProtocol {

        TCP("tcp"), IPC("ipc"), INPROC("inproc");

        private final String name;
        private final String prefix;

        ZmqProtocol(String name) {
            this.name = name;
            this.prefix = name + "://";
        }

        public String fixAddr(String addr) {
            if (!addr.startsWith(prefix))
                return prefix + addr;
            return addr;
        }

        @Override
        public String toString() {
            return name();
        }

        public static ZmqProtocol of(String name) {
            for (ZmqProtocol protocol : ZmqProtocol.values()) {
                if (protocol.name.equalsIgnoreCase(name))
                    return protocol;
                if (protocol.prefix.equalsIgnoreCase(name))
                    return protocol;
            }
            throw new IllegalArgumentException("Unsupported protocol type -> " + name);
        }

    }

    /**
     * @author yellow013
     */
    public static class ZmqAddr {

        private final ZmqProtocol protocol;

        private final String addr;

        private final String completeInfo;

        public ZmqAddr(ZmqProtocol protocol, String addr) {
            this.protocol = protocol;
            this.addr = addr;
            this.completeInfo = protocol.fixAddr(addr);
        }

        /**
         * 创建TCP协议连接
         *
         * @param port int
         * @return ZmqAddr
         */
        public static ZmqAddr tcp(int port) {
            return tcp("*", port);
        }

        /**
         * 创建TCP协议连接
         *
         * @param addr String
         * @param port int
         * @return ZmqAddr
         */
        public static ZmqAddr tcp(@Nonnull String addr, int port) {
            Asserter.nonEmpty(addr, "addr");
            Asserter.atWithinRange(port, 4096, 65536, "port");
            if (!addr.equals("*"))
                IpAddressValidator.assertIpAddress(addr);
            return new ZmqAddr(ZmqProtocol.TCP, addr + ":" + port);
        }

        /**
         * 使用[ipc]协议连接, 用于进程间通信
         *
         * @param addr String
         * @return ZmqAddr
         */
        public static ZmqAddr ipc(@Nonnull String addr) {
            Asserter.nonEmpty(addr, "addr");
            return new ZmqAddr(ZmqProtocol.IPC, addr);
        }

        /**
         * 使用[inproc]协议连接, 用于线程间通信
         *
         * @param addr String
         * @return ZmqAddr
         */
        public static ZmqAddr inproc(@Nonnull String addr) {
            Asserter.nonEmpty(addr, "addr");
            return new ZmqAddr(ZmqProtocol.INPROC, addr);
        }

        public ZmqProtocol getProtocol() {
            return protocol;
        }

        public String getAddr() {
            return addr;
        }

        public String getCompleteInfo() {
            return completeInfo;
        }

        @Override
        public String toString() {
            return completeInfo;
        }

    }

    public static void main(String[] args) {
        ZmqConfigurator configurator = ZmqConfigurator.tcp("192.168.1.1", 5551).ioThreads(3)
                .tcpKeepAlive(TcpKeepAlive.withDefault());
        System.out.println(configurator);
        System.out.println(ZmqConfigurator.getZmqVersion());
        System.out.println(IpAddressValidator.isIPv4("192.168.1.1"));
    }

}
