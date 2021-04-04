package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.transport.api.Transport;
import io.mercury.transport.configurator.TcpKeepAliveOption;
import io.mercury.transport.configurator.TransportConfigurator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

abstract class ZmqTransport0 implements Transport, Closeable {

	// ZContext
	@Getter(AccessLevel.PROTECTED)
	private ZContext ctx;

	// ZMQ.Socket
	protected ZMQ.Socket socket;

	// 组件运行状态, 初始为已开始运行
	protected AtomicBoolean isRunning = new AtomicBoolean(true);

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqTransport0.class);

	protected ZmqTransport0(int ioThreads) {
		this.ctx = new ZContext(ioThreads);
		log.info("init zmq context");
	}

	protected ZMQ.Socket initSocket(SocketType type) {
		this.socket = ctx.createSocket(type);
		log.info("create zmq socket with type -> {}", type);
		return socket;
	}

	protected ZMQ.Socket setTcpKeepAlive(TcpKeepAliveOption option) {
		if (option != null) {
			log.info("setting zmq socket tcp keep alive");
			socket.setTCPKeepAlive(option.getKeepAlive().getCode());
			socket.setTCPKeepAliveCount(option.getKeepAliveCount());                                   
			socket.setTCPKeepAliveIdle(option.getKeepAliveIdle());
			socket.setTCPKeepAliveInterval(option.getKeepAliveInterval());
		}
		return socket;
	}

	/**
	 * 用于在外部设置Socket参数
	 * 
	 * @return
	 */
	public ZMQ.Socket socketSetter() {
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
	public static abstract class ZmqConfigurator implements TransportConfigurator {

		@Getter
		private final String addr;

		@Getter
		private final int ioThreads;

		@Getter
		private final TcpKeepAliveOption tcpKeepAliveOption;

	}

}
