package io.mercury.transport.zmq;

import io.mercury.common.annotation.OnlyOverrideEquals;
import io.mercury.common.net.IpAddressValidator;
import io.mercury.common.serialization.specific.BytesSerializer;
import io.mercury.common.serialization.specific.JsonDeserializable;
import io.mercury.common.serialization.specific.JsonSerializable;
import io.mercury.serialization.json.JsonParser;
import io.mercury.serialization.json.JsonWriter;
import io.mercury.transport.TransportConfigurator;
import io.mercury.transport.attr.TcpKeepAlive;
import io.mercury.transport.attr.Topics;
import io.mercury.transport.zmq.base.ZmqAddr;
import io.mercury.transport.zmq.base.ZmqProtocol;
import org.slf4j.Logger;
import org.zeromq.ZMQ;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static io.mercury.common.lang.Asserter.greaterThan;
import static io.mercury.common.lang.Asserter.nonNull;
import static io.mercury.common.log4j2.Log4j2LoggerFactory.getLogger;
import static io.mercury.common.sys.CurrentRuntime.availableProcessors;

@OnlyOverrideEquals
public final class ZmqConfigurator implements
        TransportConfigurator, JsonSerializable, JsonDeserializable<ZmqConfigurator> {

    private static final Logger log = getLogger(ZmqConfigurator.class);

    private final ZmqAddr addr;

    private int ioThreads = 1;

    private int highWaterMark = 8192;

    private TcpKeepAlive tcpKeepAlive = null;

    /**
     * @param addr ZmqAddr
     */
    ZmqConfigurator(ZmqAddr addr) {
        this.addr = addr;
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
     * @param ioThreads int
     * @return ZmqConfigurator
     */
    public ZmqConfigurator ioThreads(int ioThreads) {
        greaterThan(ioThreads, 1, "ioThreads");
        this.ioThreads = Math.min(ioThreads, availableProcessors());
        return this;
    }

    /**
     * Set high watermark with socket
     *
     * @param highWaterMark int
     * @return ZmqConfigurator
     */
    public ZmqConfigurator highWaterMark(int highWaterMark) {
        this.highWaterMark = highWaterMark;
        return this;
    }

    /**
     * @param tcpKeepAlive TcpKeepAlive
     * @return ZmqConfigurator
     */
    public ZmqConfigurator tcpKeepAlive(@Nonnull TcpKeepAlive tcpKeepAlive) {
        nonNull(tcpKeepAlive, "tcpKeepAlive");
        this.tcpKeepAlive = tcpKeepAlive;
        return this;
    }

    /**
     * @return ZmqSender<byte [ ]>
     */
    public ZmqSender<byte[]> createSender() {
        return createSender(bytes -> bytes);
    }

    /**
     * @param <T> T type
     * @param ser BytesSerializer<T>
     * @return ZmqSender<T>
     */
    public <T> ZmqSender<T> createSender(@Nonnull BytesSerializer<T> ser) {
        nonNull(ser, "ser");
        return new ZmqSender<>(this, ser);
    }

    /**
     * @param handler Function<byte[], byte[]>
     * @return ZmqReceiver
     */
    public ZmqReceiver createReceiver(@Nonnull Function<byte[], byte[]> handler) {
        nonNull(handler, "handler");
        return new ZmqReceiver(this, handler);
    }

    /**
     * @param consumer BiConsumer<byte[], byte[]>
     * @return ZmqSubscriber
     */
    public ZmqSubscriber createSubscriber(@Nonnull BiConsumer<byte[], byte[]> consumer) {
        return createSubscriber(Topics.with(""), consumer);
    }

    /**
     * @param topics   Topics
     * @param consumer BiConsumer<byte[], byte[]>
     * @return ZmqSubscriber
     */
    public ZmqSubscriber createSubscriber(@Nonnull Topics topics,
                                          @Nonnull BiConsumer<byte[], byte[]> consumer) {
        nonNull(topics, "topics");
        nonNull(consumer, "consumer");
        return new ZmqSubscriber(this, topics, consumer);
    }

    /**
     * @return ZmqPublisher
     */
    public ZmqPublisher<byte[]> createPublisherWithBinary() {
        return createPublisherWithBinary("");
    }

    /**
     * @param topic String
     * @return ZmqPublisher
     */
    public ZmqPublisher<byte[]> createPublisherWithBinary(@Nonnull String topic) {
        return createPublisher(topic, bytes -> bytes);
    }

    /**
     * @return ZmqPublisher
     */
    public ZmqPublisher<String> createPublisherWithString() {
        return createPublisherWithString("", ZMQ.CHARSET);
    }

    /**
     * @param topic String
     * @return ZmqPublisher
     */
    public ZmqPublisher<String> createPublisherWithString(@Nonnull String topic) {
        return createPublisherWithString(topic, ZMQ.CHARSET);
    }

    /**
     * @param encode Charset
     * @return ZmqPublisher
     */
    public ZmqPublisher<String> createPublisherWithString(@Nonnull Charset encode) {
        return createPublisherWithString("", encode);
    }

    /**
     * @param topic  String
     * @param encode Charset
     * @return ZmqPublisher
     */
    public ZmqPublisher<String> createPublisherWithString(@Nonnull String topic,
                                                          @Nonnull Charset encode) {
        nonNull(encode, "encode");
        return createPublisher(topic, str -> str.getBytes(encode));
    }

    /**
     * @param <T> T type
     * @param ser BytesSerializer<T>
     * @return ZmqPublisher
     */
    public <T> ZmqPublisher<T> createPublisher(@Nonnull BytesSerializer<T> ser) {
        return createPublisher("", ser);
    }

    /**
     * @param <T>   T type
     * @param topic String
     * @param ser   BytesSerializer<T>
     * @return ZmqPublisher
     */
    public <T> ZmqPublisher<T> createPublisher(@Nonnull String topic,
                                               @Nonnull BytesSerializer<T> ser) {
        nonNull(topic, "topic");
        nonNull(ser, "ser");
        return new ZmqPublisher<>(this, topic, ser);
    }

    private transient String toStringCache;

    @Override
    public String toString() {
        if (toStringCache == null)
            this.toStringCache = JsonWriter.toJson(this);
        return toStringCache;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof ZmqConfigurator o) {
            if (!this.addr.equals(o.getAddr()))
                return false;
            if (this.ioThreads != o.getIoThreads())
                return false;
            if (this.highWaterMark != o.getHighWaterMark())
                return false;
            return this.tcpKeepAlive.equals(o.getTcpKeepAlive());
        } else
            return false;
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
        return JsonWriter.toJsonHasNulls(this);
    }

    @Nonnull
    @Override
    public ZmqConfigurator fromJson(@Nonnull String json) {
        Map<String, Object> map = JsonParser.toMap(json);
        var protocol = ZmqProtocol.of((String) map.get("protocol"));
        String addr = (String) map.get("addr");
        int ioThreads = (int) map.get("ioThreads");
        var tcpKeepAlive = JsonParser.toObject((String) map.get("tcpKeepAlive"), TcpKeepAlive.class);
        assert tcpKeepAlive != null;
        return new ZmqConfigurator(protocol.addr(addr)).ioThreads(ioThreads).tcpKeepAlive(tcpKeepAlive);
    }

    public static void main(String[] args) {
        ZmqConfigurator configurator = ZmqComponent.tcp("192.168.1.1", 5551)
                .ioThreads(3).tcpKeepAlive(TcpKeepAlive.sysDefault());
        System.out.println(configurator);
        System.out.println(ZmqConfigurator.getZmqVersion());
        System.out.println(IpAddressValidator.isIPv4("192.168.1.1"));
    }

}
