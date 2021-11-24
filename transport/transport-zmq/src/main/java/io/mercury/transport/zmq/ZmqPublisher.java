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
import io.mercury.transport.api.Publisher;
import io.mercury.transport.exception.PublishFailedException;
import io.mercury.transport.zmq.exception.ZmqBindException;

public final class ZmqPublisher<T> extends ZmqTransport implements Publisher<byte[], T>, Closeable {

	// default topic
	private final byte[] sendMore;

	private final BytesSerializer<T> ser;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqPublisher.class);

	/**
	 * @param topic
	 * @param cfg
	 * @param ser
	 */
	ZmqPublisher(@Nonnull String topic, @Nonnull ZmqConfigurator cfg, @Nonnull BytesSerializer<T> ser) {
		super(cfg);
		this.sendMore = topic.getBytes(ZMQ.CHARSET);
		this.ser = ser;
		var addr = cfg.getAddr();
		if (zSocket.bind(addr)) {
			log.info("bound addr -> {}", addr);
		} else {
			log.error("unable to bind -> {}", addr);
			throw new ZmqBindException(addr);
		}
		setTcpKeepAlive(cfg.getTcpKeepAlive());
		newStartTime();
		this.name = "ZMQ::PUB$" + addr + "/" + topic;
	}

	public BytesSerializer<T> getSerializer() {
		return ser;
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
			byte[] bytes = ser.serialization(msg);
			if (bytes != null && bytes.length > 0) {
				zSocket.sendMore(target);
				zSocket.send(bytes, ZMQ.NOBLOCK);
			}
		} else
			log.warn("ZmqPublisher -> [{}] already closed", name);
	}

	public static void main(String[] args) {

		try (ZmqPublisher<String> publisher = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(2)
				.createStringPublisher("test")) {
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
