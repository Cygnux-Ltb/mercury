package io.mercury.transport.rabbitmq;

import java.io.IOException;
import java.util.Arrays;

import io.mercury.transport.rabbitmq.configurator.RmqConnection;
import io.mercury.transport.rabbitmq.configurator.RmqPublisherConfigurator;
import io.mercury.transport.rabbitmq.declare.AmqpQueue;
import io.mercury.transport.rabbitmq.declare.ExchangeRelationship;

public class RabbitMqPublisherTest {

	public static void main(String[] args) {

		RmqConnection connection = RmqConnection.configuration("10.0.64.201", 5672, "root", "root2018").build();

		RmqPublisherConfigurator configurator = RmqPublisherConfigurator
				.configuration(connection, ExchangeRelationship.fanout("TEST_DIR")
						.bindingQueue(Arrays.asList(AmqpQueue.named("TEST_D1")), Arrays.asList("K1", "K2")))
				.setDefaultRoutingKey("K1").build();

		try (RabbitMqPublisher publisher = new RabbitMqPublisher("TEST_PUB", configurator)) {
			publisher.publish(new String("To_K1").getBytes());
			publisher.publish("K1", new String("To_K1_0").getBytes());
			publisher.publish("K2", new String("To_K2").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
