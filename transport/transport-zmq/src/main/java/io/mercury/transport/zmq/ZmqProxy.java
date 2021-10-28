package io.mercury.transport.zmq;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import io.mercury.transport.api.Proxy;
import io.mercury.transport.api.Publisher;
import io.mercury.transport.exception.ReceiverStartException;

public class ZmqProxy<U> extends ZmqTransport implements Proxy<U> {

	public ZmqProxy(ZmqConfigurator cfg) {
		super(cfg);
		boolean proxy = ZMQ.proxy(zSocket, zSocket, zSocket, zSocket);

	}

	@Override
	public Publisher<U> getUpstream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SocketType getSocketType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receive() throws ReceiverStartException {
		// TODO Auto-generated method stub

	}

	@Override
	public void reconnect() {
		// TODO Auto-generated method stub

	}

}
