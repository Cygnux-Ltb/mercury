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
import io.mercury.transport.zmq.cfg.ZmqAddress;
import io.mercury.transport.zmq.exception.ZmqConnectionException;

public class ZmqFanout extends ZmqTransport implements Receiver, Closeable {

	private final String name;

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
		setTcpKeepAlive(cfg.getTcpKeepAlive());
		this.name = "Zmq::Fanout$" + cfg.getAddr();
	}

	@Override
	public String getName() {
		return name;
	}

	public ZmqFanoutConfigurator getCfg() {
		return cfg;
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
	}

	@Override
	public void reconnect() {
		throw new UnsupportedOperationException("ZmqFanout unsupport reconnect");
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class ZmqFanoutConfigurator extends ZmqConfigurator<ZmqFanoutConfigurator> {

		private ZmqFanoutConfigurator(ZmqAddress addr) {
			super(addr);
		}

		@Override
		protected ZmqFanoutConfigurator returnSelf() {
			return this;
		}

		/**
		 * 创建TCP协议连接
		 * 
		 * @param port
		 * @return
		 */
		public final static ZmqFanoutConfigurator tcp(int port) {
			return new ZmqFanoutConfigurator(ZmqAddress.tcp(port));
		}

		/**
		 * 创建TCP协议连接
		 * 
		 * @param addr
		 * @param port
		 * @return
		 */
		public final static ZmqFanoutConfigurator tcp(@Nonnull String addr, int port) {
			return new ZmqFanoutConfigurator(ZmqAddress.tcp(addr, port));
		}

		/**
		 * 创建IPC协议连接
		 * 
		 * @param addr
		 * @return
		 */
		public final static ZmqFanoutConfigurator ipc(@Nonnull String addr) {
			return new ZmqFanoutConfigurator(ZmqAddress.ipc(addr));
		}

	}

	public static void main(String[] args) {
		try (ZmqFanout receiver = new ZmqFanout(ZmqFanoutConfigurator.tcp(5551).setIoThreads(10), (byte[] byteMsg) -> {
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
