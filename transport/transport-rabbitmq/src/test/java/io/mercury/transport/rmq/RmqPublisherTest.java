package io.mercury.transport.rmq;

import java.io.IOException;
import java.util.Arrays;

import io.mercury.transport.rmq.config.RmqConnection;
import io.mercury.transport.rmq.config.RmqPublisherConfig;
import io.mercury.transport.rmq.declare.AmqpQueue;
import io.mercury.transport.rmq.declare.ExchangeRelationship;

public class RmqPublisherTest {

	public static void main(String[] args) {

		RmqConnection connection = RmqConnection.with("10.0.64.201", 5672, "root", "root2018").build();

		RmqPublisherConfig configurator = RmqPublisherConfig
				.configuration(connection, ExchangeRelationship.fanout("TEST_DIR")
						.bindingQueues(Arrays.asList(AmqpQueue.named("TEST_D1")), Arrays.asList("K1", "K2")))
				.setDefaultRoutingKey("K1").build();

		try (RmqPublisher publisher = new RmqPublisher("TEST_PUB", configurator)) {
			publisher.publish(new String("To_K1").getBytes());
			publisher.publish("K1", new String("To_K1_0").getBytes());
			publisher.publish("K2", new String("To_K2").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
