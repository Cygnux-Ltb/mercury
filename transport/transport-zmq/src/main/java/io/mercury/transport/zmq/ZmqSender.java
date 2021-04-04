package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.zeromq.SocketType;

import io.mercury.common.serialization.spec.ByteArraySerializer;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.api.Sender;
import io.mercury.transport.configurator.TcpKeepAliveOption;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@NotThreadSafe
public class ZmqSender<T> extends ZmqTransport implements Sender<T>, Closeable {

	@Getter
	private final String name;

	@Getter
	private final ZmqSenderConfigurator cfg;

	private final ByteArraySerializer<T> ser;

	private ZmqSender(@Nonnull ZmqSenderConfigurator cfg, @Nonnull ByteArraySerializer<T> ser) {
		super(cfg);
		this.cfg = cfg;
		this.ser = ser;
		initSocket(SocketType.REQ).connect(cfg.getAddr());
		this.name = "ZMQ::REQ$" + cfg.getConnectionInfo();
	}

	@Override
	public void sent(T msg) {
		byte[] bytes = ser.serialization(msg);
		if (bytes != null && bytes.length > 0) {
			socket.send(bytes);
			socket.recv();
		}
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class ZmqSenderConfigurator extends ZmqConfigurator {

		@Getter
		private final String connectionInfo;

		private ZmqSenderConfigurator(Builder builder) {
			super(builder.addr, builder.ioThreads, builder.tcpKeepAliveOption);
			this.connectionInfo = builder.addr;
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
			private int ioThreads = 1;

			@Getter
			@Setter
			private TcpKeepAliveOption tcpKeepAliveOption = null;

			private Builder() {
			}

			public ZmqSenderConfigurator build() {
				return new ZmqSenderConfigurator(this);
			}
		}
	}

	public static void main(String[] args) {

		ZmqSenderConfigurator configurator = ZmqSenderConfigurator.newBuilder().setIoThreads(1)
				.setAddr("tcp://localhost:5551").build();

		try (ZmqSender<String> sender = new ZmqSender<String>(configurator, msg -> msg.getBytes())) {

			sender.sent("TEST MSG");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
