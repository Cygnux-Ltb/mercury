package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.zeromq.SocketType;

import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.core.api.Receiver;
import io.mercury.transport.core.configurator.TcpKeepAliveOption;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class ZmqPipeline extends ZmqTransport implements Receiver, Closeable {

	@Getter
	private final String name;

	@Getter
	private final ZmqPipelineConfigurator configurator;

	private final Function<byte[], byte[]> pipeline;

	public ZmqPipeline(@Nonnull ZmqPipelineConfigurator configurator, @Nonnull Function<byte[], byte[]> pipeline) {
		super(configurator);
		Assertor.nonNull(pipeline, "pipeline");
		this.configurator = configurator;
		this.pipeline = pipeline;
		initSocket(SocketType.REP).bind(configurator.getAddr());
		this.name = "ZMQ::REP$" + configurator.getAddr();
	}

	@Override
	public void receive() {
		while (isRunning.get()) {
			byte[] recv = socket.recv();
			byte[] sent = pipeline.apply(recv);
			if (sent != null)
				socket.send(sent);
		}

		return;
	}

	@Override
	public void reconnect() {
		throw new UnsupportedOperationException("ZmqPipeline unsupport reconnect");
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class ZmqPipelineConfigurator extends ZmqConfigurator {

		@Getter
		private final String connectionInfo;

		private ZmqPipelineConfigurator(Builder builder) {
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

			public ZmqPipelineConfigurator build() {
				return new ZmqPipelineConfigurator(this);
			}
		}
	}

	public static void main(String[] args) {
		try (ZmqPipeline receiver = new ZmqPipeline(
				ZmqPipelineConfigurator.newBuilder().setIoThreads(10).setAddr("tcp://*:5551").build(),
				(byte[] byteMsg) -> {
					System.out.println(new String(byteMsg));
					return null;
				})) {
			Threads.sleep(15000);
			Threads.startNewThread(() -> receiver.receive());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
