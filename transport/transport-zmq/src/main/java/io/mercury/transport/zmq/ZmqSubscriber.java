package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import org.zeromq.SocketType;

import io.mercury.common.character.Charsets;
import io.mercury.common.util.Assertor;
import io.mercury.common.util.StringUtil;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.core.api.Subscriber;
import io.mercury.transport.core.configurator.TcpKeepAliveOption;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public final class ZmqSubscriber extends ZmqTransport implements Subscriber, Closeable, Runnable {

	// topics
	@Getter
	private final String[] topics;

	@Getter
	private String name;

	@Getter
	private ZmqSubConfigurator configurator;

	private final BiConsumer<byte[], byte[]> consumer;

	public ZmqSubscriber(@Nonnull ZmqSubConfigurator configurator, @Nonnull BiConsumer<byte[], byte[]> consumer) {
		super(configurator);
		Assertor.nonNull(consumer, "consumer");
		this.configurator = configurator;
		this.consumer = consumer;
		this.topics = configurator.topics;
		initSocket(SocketType.SUB).connect(configurator.getAddr());
		for (String topic : topics) {
			socket.subscribe(topic.getBytes(Charsets.UTF8));
		}
		setTcpKeepAlive(configurator.getTcpKeepAliveOption());
		this.name = "ZMQ::SUB$" + configurator.connectionInfo;
	}
	
	

	@Override
	public void subscribe() {
		while (isRunning.get()) {
			byte[] topic = socket.recv();
			byte[] msg = socket.recv();
			consumer.accept(topic, msg);
		}
	}

	@Override
	public void reconnect() {
		throw new UnsupportedOperationException("ZmqSubscriber unsupport reconnect");
	}

	@Override
	public void run() {
		subscribe();
	}

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
			private String[] topics = new String[] { "" };

			@Getter
			@Setter
			private int ioThreads = 1;

			@Getter
			@Setter
			private TcpKeepAliveOption tcpKeepAliveOption = new TcpKeepAliveOption().setTcpKeepAlive(1)
					.setTcpKeepAliveCount(10).setTcpKeepAliveIdle(30).setTcpKeepAliveInterval(30);

			private Builder() {
			}

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

		ZmqSubConfigurator configurator = ZmqSubConfigurator.newBuilder().setAddr("tcp://127.0.0.1:13001")
				.setTopics("command").setIoThreads(2).build();

		try (ZmqSubscriber subscriber = new ZmqSubscriber(configurator,
				(topic, msg) -> System.out.println(new String(topic) + "->" + new String(msg)))) {
			subscriber.subscribe();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
