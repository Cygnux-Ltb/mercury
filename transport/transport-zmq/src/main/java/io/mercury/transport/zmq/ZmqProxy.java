package io.mercury.transport.zmq;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class ZmqProxy<U> extends ZmqTransport  {

	public ZmqProxy(ZmqConfigurator cfg) {
		super(cfg);
		boolean proxy = ZMQ.proxy(socket, socket, socket, socket);
		// 測試統配符號操作
	}
	
	
	

	
	protected SocketType getSocketType() {
		return SocketType.DEALER;
	}


}
