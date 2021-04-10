package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.api.Transport;
import io.mercury.transport.configurator.TcpKeepAlive;
import io.mercury.transport.configurator.TransportConfigurator;
import io.mercury.transport.zmq.cfg.ZmqAddress;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

abstract class ZmqTransport implements Transport, Closeable {

	// ZContext
	protected ZContext ctx;

	// ZMQ.Socket
	@Getter
	protected ZMQ.Socket socket;

	@Getter
	protected final ZmqAddress addr;

	// 组件运行状态, 初始为已开始运行
	protected AtomicBoolean isRunning = new AtomicBoolean(true);

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqTransport.class);

	protected ZmqTransport(ZmqAddress addr, int ioThreads) {
		this.ctx = new ZContext(ioThreads);
		this.addr = addr;
		log.info("zmq context initialized, ioThreads=={}", ioThreads);
		SocketType type = getSocketType();
		this.socket = ctx.createSocket(type);
		log.info("zmq socket created with type -> {}", type);
	}

	@AbstractFunction
	protected abstract SocketType getSocketType();

	/**
	 * 设置TcpKeepAlive, 由子类调用
	 *
	 * @param option
	 * @return
	 */
	protected ZMQ.Socket setTcpKeepAlive(TcpKeepAlive tcpKeepAlive) {
		if (tcpKeepAlive != null) {
			log.info("setting zmq socket tcp keep alive");
			socket.setTCPKeepAlive(tcpKeepAlive.getKeepAlive().getCode());
			socket.setTCPKeepAliveCount(tcpKeepAlive.getKeepAliveCount());
			socket.setTCPKeepAliveIdle(tcpKeepAlive.getKeepAliveIdle());
			socket.setTCPKeepAliveInterval(tcpKeepAlive.getKeepAliveInterval());
		}
		return socket;
	}

	@Override
	public boolean isConnected() {
		return !ctx.isClosed();
	}

	@Override
	public void close() throws IOException {
		destroy();
	}

	@Override
	public boolean destroy() {
		if (isRunning.compareAndSet(true, false)) {
			socket.close();
			ctx.close();
		}
		log.info("zmq transport destroy");
		return ctx.isClosed();
	}

	@RequiredArgsConstructor
	public static abstract class ZmqConfigurator<T extends ZmqConfigurator<T>> implements TransportConfigurator {

		@Getter
		private final ZmqAddress addr;

		@Getter
		private int ioThreads = 1;

		@Getter
		private TcpKeepAlive tcpKeepAlive = null;

		public T setIoThreads(int ioThreads) {
			this.ioThreads = ioThreads;
			return returnSelf();
		}

		public T setTcpKeepAlive(TcpKeepAlive tcpKeepAlive) {
			this.tcpKeepAlive = tcpKeepAlive;
			return returnSelf();
		}

		@Override
		public String getConnectionInfo() {
			return addr.getAddr();
		}

		@Override
		public String getConfiguratorInfo() {
			return toString();
		}

		protected abstract T returnSelf();

		private transient String strCache;

		@Override
		public String toString() {
			if (strCache == null)
				this.strCache = JsonWrapper.toJson(this);
			return strCache;
		}

	}

}
