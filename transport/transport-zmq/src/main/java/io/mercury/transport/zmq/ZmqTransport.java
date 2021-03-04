package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import io.mercury.common.util.Assertor;
import io.mercury.transport.core.api.TransportModule;
import io.mercury.transport.core.configurator.TcpKeepAliveOption;
import io.mercury.transport.core.configurator.TransportConfigurator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

abstract class ZmqTransport implements TransportModule, Closeable {

	// ZContext
	@Getter(AccessLevel.PROTECTED)
	private ZContext ctx;

	protected ZMQ.Socket socket;

	protected AtomicBoolean isRunning = new AtomicBoolean(true);

	protected ZmqTransport(ZmqConfigurator configurator) {
		Assertor.nonNull(configurator, "configurator");
		this.ctx = new ZContext(configurator.ioThreads);
	}

	protected ZMQ.Socket initSocket(SocketType type) {
		this.socket = ctx.createSocket(type);
		return socket;
	}

	protected ZMQ.Socket setTcpKeepAlive(TcpKeepAliveOption option) {
		if (option != null) {
			socket.setTCPKeepAlive(option.getTcpKeepAlive());
			socket.setTCPKeepAliveCount(option.getTcpKeepAliveCount());
			socket.setTCPKeepAliveIdle(option.getTcpKeepAliveIdle());
			socket.setTCPKeepAliveInterval(option.getTcpKeepAliveInterval());
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
