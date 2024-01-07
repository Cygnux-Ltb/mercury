package io.mercury.transport.zmq;

import io.mercury.common.lang.Asserter;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.serialization.specific.BytesSerializer;
import io.mercury.common.thread.SleepSupport;
import io.mercury.transport.api.Publisher;
import io.mercury.transport.exception.PublishFailedException;
import io.mercury.transport.zmq.exception.ZmqBindException;
import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.util.Random;

public final class ZmqPublisher<T> extends ZmqTransport implements Publisher<byte[], T>, Closeable {

    private static final Logger log = Log4j2LoggerFactory.getLogger(ZmqPublisher.class);

    // default topic
    private final byte[] sendMore;

    private final BytesSerializer<T> ser;

    /**
     * @param cfg   ZmqConfigurator
     * @param topic String
     * @param ser   BytesSerializer<T>
     */
    ZmqPublisher(@Nonnull ZmqConfigurator cfg, @Nonnull String topic, @Nonnull BytesSerializer<T> ser) {
        super(cfg);
        Asserter.nonNull(topic, "topic");
        Asserter.nonNull(ser, "ser");
        this.sendMore = topic.getBytes(ZMQ.CHARSET);
        this.ser = ser;
        String addr = cfg.getAddr().toString();
        if (socket.bind(addr)) {
            log.info("ZmqPublisher bound addr -> {}", addr);
        } else {
            log.error("ZmqPublisher unable to bind -> {}", addr);
            throw new ZmqBindException(addr);
        }
        setTcpKeepAlive(cfg.getTcpKeepAlive());
        this.name = STR."zpub$\{addr}/\{topic}";
        newStartTime();
    }

    public BytesSerializer<T> getSerializer() {
        return ser;
    }

    @Override
    protected SocketType getSocketType() {
        return SocketType.PUB;
    }

    @Override
    public ZmqType getZmqType() {
        return ZmqType.ZmqPublisher;
    }

    @Override
    public void publish(@Nonnull T msg) {
        publish(sendMore, msg);
    }

    @Override
    public void publish(@Nonnull byte[] target, @Nonnull T msg) throws PublishFailedException {
        if (isRunning.get()) {
            byte[] bytes = ser.serialization(msg);
            if (bytes != null && bytes.length > 0) {
                socket.sendMore(target);
                socket.send(bytes, ZMQ.NOBLOCK);
            }
        } else
            log.warn("ZmqPublisher -> [{}] already closed", name);
    }

    public static void main(String[] args) {
        try (ZmqPublisher<String> publisher = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(2)
                .newPublisherWithString("test")) {
            Random random = new Random();
            while (true) {
                publisher.publish(String.valueOf(random.nextInt()));
                SleepSupport.sleep(1000);
            }
        } catch (Exception _) {
        }

    }

}
