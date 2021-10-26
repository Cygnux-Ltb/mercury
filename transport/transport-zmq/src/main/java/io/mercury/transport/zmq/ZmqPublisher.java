package io.mercury.transport.zmq;

import java.io.Closeable;
import java.util.Random;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.BytesSerializer;
import io.mercury.common.thread.Threads;
import io.mercury.transport.api.Publisher;
import io.mercury.transport.zmq.exception.ZmqBindException;

public final class ZmqPublisher<T> extends ZmqTransport implements Publisher<T>, Closeable {

	// default topic

	private final String topic;

	private final BytesSerializer<T> ser;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqPublisher.class);

	/**
	 * 
	 * @param cfg
	 * @param ser
	 */
	ZmqPublisher(@Nonnull String topic, @Nonnull ZmqConfigurator cfg, @Nonnull BytesSerializer<T> ser) {
		super(cfg);
		this.topic = topic;
		this.ser = ser;
		String addr = cfg.getAddr();
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

	@Override
	public SocketType getSocketType() {
		return SocketType.PUB;
	}

	@Override
	public void publish(T msg) {
		publish(topic, msg);
	}

	@Override
	public void publish(String target, T msg) {
		if (isRunning.get()) {
			byte[] bytes = ser.serialization(msg);
			if (bytes != null && bytes.length > 0) {
				zSocket.sendMore(target);
				zSocket.send(bytes, ZMQ.NOBLOCK);
			}
		} else {
			log.warn("ZmqPublisher -> [{}] already closed", name);
		}
	}

	public static void main(String[] args) {

		try (ZmqPublisher<String> publisher = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(2)
				.buildStringPublisher("commond")) {
			Random random = new Random();

			for (;;) {
				publisher.publish(String.valueOf(random.nextInt()));
				Threads.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
