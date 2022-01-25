package io.mercury.transport.zmq;

import static io.mercury.transport.zmq.ZmqConfigurator.ZmqProtocol.IPC;

import org.junit.Test;

import io.mercury.transport.zmq.annotation.ZmqSubscribe;

public class ZmqSubscriberTest {

	@Test
	public void test() {
		
	}
	
	@ZmqSubscribe(protocol = IPC, addr = "")
	private void handerZmqMsg() {
		
	}

}
