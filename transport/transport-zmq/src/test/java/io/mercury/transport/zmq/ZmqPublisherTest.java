package io.mercury.transport.zmq;

import java.util.Random;

import org.junit.Test;

import io.mercury.common.thread.SleepSupport;

public class ZmqPublisherTest {

	@Test
	public void test() {

		try (var publisher = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(2).newPublisherWithString("test")) {
			Random random = new Random();
			for (;;) {
				publisher.publish(String.valueOf(random.nextInt()));
				SleepSupport.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
