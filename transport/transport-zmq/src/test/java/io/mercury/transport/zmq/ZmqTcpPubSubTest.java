package io.mercury.transport.zmq;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.ThreadSupport;
import io.mercury.transport.attr.Topics;

public class ZmqTcpPubSubTest {

	@Test
	public void test() {
		System.out.println("ZmqTcpPubSubTest Test Start");

		String topic = "tcp-test";

		ThreadSupport.startNewThread(() -> {
			try (ZmqPublisher<String> publisher = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(1)
					.newPublisherWithString(topic)) {
				SleepSupport.sleep(2000);
				Random random = new Random();
				for (int i = 0; i < 20; i++) {
					publisher.publish(String.valueOf(random.nextInt()));
					SleepSupport.sleep(200);
				}
				publisher.publish("end");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		try (ZmqSubscriber subscriber = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(1).newSubscriber(Topics.with(topic),
				this::handleMag)) {
			subscriber.subscribe();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handleMag(byte[] topic, byte[] msg) {
		String str = new String(msg);
		System.out.println(new String(topic) + " -> " + str);
		if (str.equalsIgnoreCase("end"))
			System.exit(0);
	}

}
