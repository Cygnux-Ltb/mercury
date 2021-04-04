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
import io.mercury.common.serialization.spec.ByteArraySerializer;
import io.mercury.common.thread.Threads;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.api.Publisher;
import io.mercury.transport.configurator.TcpKeepAliveOption;
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

	private final ByteArraySerializer<T> ser;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqPublisher.class);

	/**
	 * 
	 * @param cfg
	 * @param ser
	 */
	private ZmqPublisher(@Nonnull ZmqPubConfigurator cfg, @Nonnull ByteArraySerializer<T> ser) {
		super(cfg);
		this.cfg = cfg;
		this.ser = ser;
		this.defaultTopic = cfg.getDefaultTopic();
		initSocket(SocketType.PUB).bind(cfg.getAddr());
		setTcpKeepAlive(cfg.getTcpKeepAliveOption());
		this.name = "ZMQ::PUB$" + cfg.connectionInfo;
	}

	/**
	 * 
	 * @param configurator
	 * @return
	 */
	public static ZmqPublisher<String> newPublisher(@Nonnull ZmqPubConfigurator configurator) {
		return newPublisher(configurator, Charsets.UTF8);
	}

	/**
	 * 
	 * @param configurator
	 * @param charset
	 * @return
	 */
	public static ZmqPublisher<String> newPublisher(@Nonnull ZmqPubConfigurator configurator, Charset charset) {
		return new ZmqPublisher<>(configurator, str -> str.getBytes(charset));
	}

	/**
	 * 
	 * @param <T>
	 * @param configurator
	 * @param serializer
	 * @return
	 */
	public static <T> ZmqPublisher<T> newPublisher(@Nonnull ZmqPubConfigurator configurator,
			@Nonnull ByteArraySerializer<T> serializer) {
		return new ZmqPublisher<>(configurator, serializer);
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
			log.warn("ZmqPublisher -> {} has exited", name);
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

		@Getter
		private final String connectionInfo;

		private ZmqPubConfigurator(Builder builder) {
			super(builder.addr, builder.ioThreads, builder.tcpKeepAliveOption);
			this.defaultTopic = builder.defaultTopic;
			this.connectionInfo = builder.addr + "/" + defaultTopic;
		}

		public static Builder newBuilder() {
			return new Builder();
		}

		@Override
		public String getConfiguratorInfo() {
			return toString();
		}

		private transient String toStringCache;

		@Override
		public String toString() {
			if (toStringCache == null)
				this.toStringCache = JsonWrapper.toJson(this);
			return toStringCache;
		}

		@Accessors(chain = true)
		public static class Builder {

			@Getter
			@Setter
			private String addr = "tcp://*:5555";

			@Getter
			@Setter
			private String defaultTopic = "";

			@Getter
			@Setter
			private int ioThreads = 1;

			@Getter
			@Setter
			private TcpKeepAliveOption tcpKeepAliveOption = null;

			private Builder() {
			}

			public ZmqPubConfigurator build() {
				return new ZmqPubConfigurator(this);
			}
		}
	}

	public static void main(String[] args) {

		ZmqPubConfigurator configurator = ZmqPubConfigurator.newBuilder().setAddr("tcp://127.0.0.1:13001")
				.setDefaultTopic("command").setIoThreads(2).build();

		try (ZmqPublisher<String> publisher = ZmqPublisher.newPublisher(configurator)) {
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
