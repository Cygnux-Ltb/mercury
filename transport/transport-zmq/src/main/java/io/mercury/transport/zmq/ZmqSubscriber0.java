package io.mercury.transport.zmq;

import static io.mercury.transport.configurator.TcpKeepAliveOption.KeepAlive.Enable;
import static lombok.AccessLevel.PRIVATE;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.character.Charsets;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;
import io.mercury.common.util.StringUtil;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.api.Subscriber;
import io.mercury.transport.configurator.TcpKeepAliveOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

public final class ZmqSubscriber0 extends ZmqTransport implements Subscriber, Closeable, Runnable {

	// topics
	@Getter
	private final String[] topics;

	@Getter
	private final String name;

	@Getter
	private final ZmqSubConfigurator cfg;

	private final BiConsumer<byte[], byte[]> consumer;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqSubscriber0.class);

	public ZmqSubscriber0(@Nonnull ZmqSubConfigurator cfg, @Nonnull BiConsumer<byte[], byte[]> consumer) {
		super(cfg);
		Assertor.nonNull(consumer, "consumer");
		this.cfg = cfg;
		this.consumer = consumer;
		this.topics = cfg.topics;
		initSocket(SocketType.SUB).connect(cfg.getAddr());
		setTcpKeepAlive(cfg.getTcpKeepAliveOption());
		for (String topic : topics) {
			socket.subscribe(topic.getBytes(Charsets.UTF8));
		}
		this.name = "ZMQ::SUB$" + cfg.connectionInfo;

	}

	@Override
	public void subscribe() {
		while (isRunning.get()) {
			byte[] topic = socket.recv();
			byte[] msg = socket.recv();
			consumer.accept(topic, msg);
		}
		log.warn("ZmqSubscriber -> {} has exited", name);
	}

	@Override
	public void reconnect() {
		throw new UnsupportedOperationException("ZmqSubscriber unsupport reconnect");
	}

	@Override
	public void run() {
		subscribe();
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class ZmqSubConfigurator extends ZmqConfigurator {

		@Getter
		private final String[] topics;
		@Getter
		private final String connectionInfo;

		private ZmqSubConfigurator(Builder builder) {
			super(builder.addr, builder.ioThreads, builder.tcpKeepAliveOption);
			this.topics = builder.topics;
			this.connectionInfo = builder.addr + "/" + StringUtil.toString(topics);
		}

		public static Builder tcp(int port) {
			return tcp("*", port);
		}

		public static Builder tcp(@Nonnull String addr, int port) {
			return new Builder("tcp://" + addr + ":" + port);
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
		@RequiredArgsConstructor(access = PRIVATE)
		public static class Builder {

			@Getter
			private final String addr;

			@Getter
			private String[] topics = new String[] { "" };

			@Getter
			@Setter
			private int ioThreads = 1;

			@Getter
			@Setter
			private TcpKeepAliveOption tcpKeepAliveOption = new TcpKeepAliveOption().setKeepAlive(Enable)
					.setKeepAliveCount(10).setKeepAliveIdle(30).setKeepAliveInterval(30);

			public Builder setTopics(String... topics) {
				this.topics = topics;
				return this;
			}

			public ZmqSubConfigurator build() {
				return new ZmqSubConfigurator(this);
			}
		}
	}

	public static void main(String[] args) {

		ZmqSubConfigurator configurator = ZmqSubConfigurator.tcp("127.0.0.1", 13001).setTopics("command")
				.setIoThreads(2).build();

		try (ZmqSubscriber0 subscriber = new ZmqSubscriber0(configurator,
				(topic, msg) -> System.out.println(new String(topic) + "->" + new String(msg)))) {
			subscriber.subscribe();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
