package io.mercury.transport.zmq;

import java.io.Closeable;
import java.nio.charset.Charset;
import java.util.Random;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import io.mercury.common.character.Charsets;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.spec.BytesSerializer;
import io.mercury.common.thread.Threads;
import io.mercury.transport.api.Publisher;
import io.mercury.transport.configurator.TcpKeepAliveOption;
import io.mercury.transport.zmq.cfg.ZmqAddress;
import io.mercury.transport.zmq.exception.ZmqConnectionException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public final class ZmqPublisher<T> extends ZmqTransport implements Publisher<T>, Closeable {

	// default topic
	@Getter
	private final String defaultTopic;

	@Getter
	private final String name;

	@Getter
	private final ZmqPubConfigurator cfg;

	private final BytesSerializer<T> ser;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqPublisher.class);

	/**
	 * 
	 * @param cfg
	 * @param ser
	 */
	private ZmqPublisher(@Nonnull ZmqPubConfigurator cfg, @Nonnull BytesSerializer<T> ser) {
		super(cfg.getAddr(), cfg.getIoThreads());
		this.cfg = cfg;
		this.ser = ser;
		this.defaultTopic = cfg.getDefaultTopic();
		if (socket.bind(addr.getAddr())) {
			log.info("bound addr -> {}", addr);
		} else {
			log.error("unable to bind -> {}", addr);
			throw new ZmqConnectionException(addr);
		}
		setTcpKeepAlive(cfg.getTcpKeepAliveOption());
		this.name = "Zmq::Pub$" + cfg.getConnectionInfo();
	}

	/**
	 * 
	 * @param cfg
	 * @return
	 */
	public static ZmqPublisher<String> create(@Nonnull ZmqPubConfigurator cfg) {
		return create(cfg, Charsets.UTF8);
	}

	/**
	 * 
	 * @param cfg
	 * @param charset
	 * @return
	 */
	public static ZmqPublisher<String> create(@Nonnull ZmqPubConfigurator cfg, Charset charset) {
		return new ZmqPublisher<>(cfg, str -> str.getBytes(charset));
	}

	/**
	 * 
	 * @param <T>
	 * @param cfg
	 * @param ser
	 * @return
	 */
	public static <T> ZmqPublisher<T> create(@Nonnull ZmqPubConfigurator cfg, @Nonnull BytesSerializer<T> ser) {
		return new ZmqPublisher<>(cfg, ser);
	}

	@Override
	protected SocketType getSocketType() {
		return SocketType.PUB;
	}

	@Override
	public void publish(T msg) {
		publish(defaultTopic, msg);
	}

	@Override
	public void publish(String target, T msg) {
		if (isRunning.get()) {
			byte[] bytes = ser.serialization(msg);
			if (bytes != null && bytes.length > 0) {
				socket.sendMore(target);
				socket.send(bytes, ZMQ.NOBLOCK);
			}
		} else {
			log.warn("ZmqPublisher -> [{}] already closed", name);
		}
	}

	@Override
	public String getPublisherName() {
		return name;
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class ZmqPubConfigurator extends ZmqConfigurator {

		@Getter
		private final String defaultTopic;

		private ZmqPubConfigurator(Builder builder) {
			super(builder.addr, builder.ioThreads, builder.tcpKeepAliveOption);
			this.defaultTopic = builder.defaultTopic;
		}

		/**
		 * 创建TCP协议连接
		 * 
		 * @param port
		 * @return
		 */
		public final static Builder tcp(int port) {
			return new Builder(ZmqAddress.tcp(port));
		}

		/**
		 * 创建TCP协议连接
		 * 
		 * @param addr
		 * @param port
		 * @return
		 */
		public final static Builder tcp(String addr, int port) {
			return new Builder(ZmqAddress.tcp(addr, port));
		}

		/**
		 * 创建IPC协议连接
		 * 
		 * @param addr
		 * @return
		 */
		public final static Builder ipc(String addr) {
			return new Builder(ZmqAddress.ipc(addr));
		}

		@Accessors(chain = true)
		public static class Builder {

			private final ZmqAddress addr;

			@Setter
			private String defaultTopic = "";

			@Setter
			private int ioThreads = 1;

			@Setter
			private TcpKeepAliveOption tcpKeepAliveOption = null;

			private Builder(ZmqAddress addr) {
				this.addr = addr;
			}

			public ZmqPubConfigurator build() {
				return new ZmqPubConfigurator(this);
			}
		}
	}

	public static void main(String[] args) {

		ZmqPubConfigurator configurator = ZmqPubConfigurator.tcp("127.0.0.1", 13001).setDefaultTopic("command")
				.setIoThreads(2).build();

		try (ZmqPublisher<String> publisher = ZmqPublisher.create(configurator)) {
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
