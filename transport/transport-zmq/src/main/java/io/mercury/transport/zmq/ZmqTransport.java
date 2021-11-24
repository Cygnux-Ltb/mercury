package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;
import io.mercury.transport.api.Transport;
import io.mercury.transport.api.TransportComponent;
import io.mercury.transport.configurator.TcpKeepAlive;

abstract class ZmqTransport extends TransportComponent implements Transport, Closeable {

	protected final ZmqConfigurator cfg;

	// 组件运行状态, 初始为已开始运行
	protected final AtomicBoolean isRunning = new AtomicBoolean(true);

	// ZContext
	protected ZContext zCtx;

	// ZMQ.Socket
	protected ZMQ.Socket zSocket;

	protected String name;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqTransport.class);

	protected ZmqTransport(final ZmqConfigurator cfg) {
		Assertor.nonNull(cfg, "cfg");
		this.cfg = cfg;
		this.zCtx = new ZContext(cfg.getIoThreads());
		log.info("zmq context initialized, ioThreads=={}", cfg.getIoThreads());
		var type = getSocketType();
		this.zSocket = zCtx.createSocket(type);
		log.info("ZMQ socket created with type -> {}", type);
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
			zSocket.setTCPKeepAlive(tcpKeepAlive.getKeepAlive().getCode());
			zSocket.setTCPKeepAliveCount(tcpKeepAlive.getKeepAliveCount());
			zSocket.setTCPKeepAliveIdle(tcpKeepAlive.getKeepAliveIdle());
			zSocket.setTCPKeepAliveInterval(tcpKeepAlive.getKeepAliveInterval());
		}
		return zSocket;
	}

	public ZmqConfigurator getConfigurator() {
		return cfg;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isConnected() {
		return !zCtx.isClosed();
	}

	@Override
	public void close() throws IOException {
		closeIgnoreException();
	}

	@Override
	public boolean closeIgnoreException() {
		if (isRunning.compareAndSet(true, false)) {
			zSocket.close();
			zCtx.close();
		}
		newEndTime();
		log.info("zmq transport closed");
		return zCtx.isClosed();
	}

}
