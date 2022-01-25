package io.mercury.transport.zmq;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.zeromq.ZMQ;

public final class ZmqProxy {

	private ZmqProxy(
			// 代理前端
			@Nonnull ZmqTransport frontend,
			// 代理后端
			@Nonnull ZmqTransport backend,
			// If the capture socket is not NULL, the proxy shall send all messages,
			// received on both frontend and backend, to the capture socket.
			// The capture socket should be a ZMQ_PUB, ZMQ_DEALER, ZMQ_PUSH, or ZMQ_PAIR
			// socket.
			@Nullable ZmqTransport capture,
			// Proxy控制管道, 用于接收控制指令
			@Nullable ZmqTransport control) {
		boolean succeed = ZMQ.proxy(frontend.getSocket(), backend.getSocket(), capture.getSocket(),
				control.getSocket());

	}

	public static class B {

	}

}
