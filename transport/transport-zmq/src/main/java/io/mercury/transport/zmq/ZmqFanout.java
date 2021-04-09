package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.transport.api.Receiver;
import io.mercury.transport.configurator.TcpKeepAliveOption;
import io.mercury.transport.zmq.cfg.ZmqAddress;
import io.mercury.transport.zmq.exception.ZmqConnectionException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class ZmqFanout extends ZmqTransport implements Receiver, Closeable {

	@Getter
	private final String name;

	@Getter
	private final ZmqFanoutConfigurator cfg;

	private final Function<byte[], byte[]> pipeline;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqPublisher.class);

	public ZmqFanout(@Nonnull ZmqFanoutConfigurator cfg, @Nonnull Function<byte[], byte[]> pipeline) {
		super(cfg.getAddr(), cfg.getIoThreads());
		Assertor.nonNull(pipeline, "pipeline");
		this.cfg = cfg;
		this.pipeline = pipeline;
		if (socket.bind(addr.getAddr())) {
			log.info("bound addr -> {}", addr);
		} else {
			log.error("unable to bind -> {}", addr);
			throw new ZmqConnectionException(addr);
		}
		setTcpKeepAlive(cfg.getTcpKeepAliveOption());
		this.name = "Zmq::Fanout$" + cfg.getAddr();
	}

	@Override
	protected SocketType getSocketType() {
		return SocketType.REP;
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
	public static final class ZmqFanoutConfigurator extends ZmqConfigurator {

		private ZmqFanoutConfigurator(Builder builder) {
			super(builder.addr, builder.ioThreads, builder.tcpKeepAliveOption);
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
			private int ioThreads = 1;

			@Setter
			private TcpKeepAliveOption tcpKeepAliveOption = null;

			private Builder(ZmqAddress addr) {
				this.addr = addr;
			}

			public ZmqFanoutConfigurator build() {
				return new ZmqFanoutConfigurator(this);
			}
		}
	}

	public static void main(String[] args) {
		try (ZmqFanout receiver = new ZmqFanout(ZmqFanoutConfigurator.tcp(5551).setIoThreads(10).build(),
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
