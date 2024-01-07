package io.mercury.transport.zmq;

import io.mercury.common.lang.Asserter;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.transport.api.Subscriber;
import io.mercury.transport.attr.TcpKeepAlive;
import io.mercury.transport.attr.Topics;
import io.mercury.transport.zmq.exception.ZmqConnectionException;
import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * @author yellow013
 */
public final class ZmqSubscriber extends ZmqTransport implements Subscriber {

    private static final Logger log = Log4j2LoggerFactory.getLogger(ZmqSubscriber.class);

    // topics
    private final Topics topics;

    // 订阅消息消费者
    private final BiConsumer<byte[], byte[]> consumer;

    ZmqSubscriber(@Nonnull ZmqConfigurator cfg, @Nonnull Topics topics,
                  @Nonnull BiConsumer<byte[], byte[]> consumer)
            throws ZmqConnectionException {
        super(cfg);
        Asserter.nonNull(topics, "topics");
        Asserter.nonNull(consumer, "consumer");
        this.topics = topics;
        this.consumer = consumer;
        String addr = cfg.getAddr().toString();
        if (socket.connect(addr))
            log.info("ZmqSubscriber connected addr -> {}", addr);
        else {
            log.error("ZmqSubscriber unable to connect addr -> {}", addr);
            throw new ZmqConnectionException(addr);
        }
        setTcpKeepAlive(cfg.getTcpKeepAlive() == null
                // 如果TcpKeepAlive为空, 使用默认TcpKeepAlive配置
                ? TcpKeepAlive.enable().setKeepAliveCount(10).setKeepAliveIdle(30).setKeepAliveInterval(30)
                : cfg.getTcpKeepAlive());
        topics.each(topic -> socket.subscribe(topic.getBytes(ZMQ.CHARSET)));
        this.name = STR."zsub$\{addr}/\{topics}";
        newStartTime();
    }

    public Topics getTopics() {
        return topics;
    }

    @Override
    protected SocketType getSocketType() {
        return SocketType.SUB;
    }

    @Override
    public ZmqType getZmqType() {
        return ZmqType.ZmqSubscriber;
    }

    @Override
    public void subscribe() {
        while (isRunning.get()) {
            byte[] topic = socket.recv();
            log.debug("received topic, length: {}", topic.length);
            byte[] msg = socket.recv();
            log.debug("received msg, length: {}", topic.length);
            consumer.accept(topic, msg);
        }
        log.info("ZmqSubscriber -> [{}] already closed", name);
    }

    @Override
    public void reconnect() {
        throw new UnsupportedOperationException("ZmqSubscriber unsupported reconnect");
    }

    @Override
    public void run() {
        subscribe();
    }

    public static void main(String[] args) {
        try (ZmqSubscriber subscriber = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(2).newSubscriber(
                Topics.with("test"),
                (topic, msg) -> System.out.println(STR."\{new String(topic)} -> \{new String(msg)}"))) {
            subscriber.subscribe();
        } catch (IOException _) {
        }
    }

}
