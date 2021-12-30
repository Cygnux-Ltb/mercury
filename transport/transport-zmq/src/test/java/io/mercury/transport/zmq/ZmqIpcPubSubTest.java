package io.mercury.transport.zmq;

import java.io.IOException;

import org.junit.Test;

import io.mercury.transport.configurator.Topics;

public class ZmqSubscriberTest {

	@Test
	public void test() {

		try (var subscriber = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(2).newSubscriber(Topics.with("test"),
				this::handleMag)) {
			subscriber.subscribe();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handleMag(byte[] topic, byte[] msg) {
		System.out.println(new String(topic) + " -> " + new String(msg));
	}

}
