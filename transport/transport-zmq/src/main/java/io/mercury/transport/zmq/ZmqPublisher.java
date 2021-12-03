package io.mercury.transport.zmq;

import java.io.Closeable;
import java.util.Random;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.BytesSerializer;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.util.Assertor;
import io.mercury.transport.api.Publisher;
import io.mercury.transport.exception.PublishFailedException;
import io.mercury.transport.zmq.exception.ZmqBindException;

public final class ZmqPublisher<T> extends ZmqTransport implements Publisher<byte[], T>, Closeable {

	// default topic
	private final byte[] sendMore;

	private final BytesSerializer<T> serializer;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqPublisher.class);

	/**
	 * @param cfg
	 * @param topic
	 * @param serializer
	 */
	ZmqPublisher(@Nonnull ZmqConfigurator cfg, @Nonnull String topic, @Nonnull BytesSerializer<T> serializer) {
		super(cfg);
		Assertor.nonNull(topic, "topic");
		Assertor.nonNull(serializer, "serializer");
		this.sendMore = topic.getBytes(ZMQ.CHARSET);
		this.serializer = serializer;
		var addr = cfg.getAddr();
		if (socket.bind(addr))
			log.info("ZmqPublisher bound addr -> {}", addr);
		else {
			log.error("ZmqPublisher unable to bind -> {}", addr);
			throw new ZmqBindException(addr);
		}
		setTcpKeepAlive(cfg.getTcpKeepAlive());
		this.name = "ZMQ::PUB$" + addr + "/" + topic;
		newStartTime();
	}

	public BytesSerializer<T> getSerializer() {
		return serializer;
	}

	@Override
	protected SocketType getSocketType() {
		return SocketType.PUB;
	}

	@Override
	public void publish(T msg) {
		publish(sendMore, msg);
	}

	@Override
	public void publish(byte[] target, T msg) throws PublishFailedException {
		if (isRunning.get()) {
			byte[] bytes = serializer.serialization(msg);
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
			for (;;) {
				publisher.publish(String.valueOf(random.nextInt()));
				SleepSupport.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
