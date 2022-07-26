package com.rabbitmq.example;

import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

public class EmitLogHeader {

	private static final String EXCHANGE_NAME = "header_test";

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Usage: EmitLogHeader message queueName [headers]...");
			System.exit(1);
		}

		// The API requires a routing key, but in fact if you are using a header
		// exchange the
		// value of the routing key is not used in the routing. You can store
		// information
		// for the receiver here as the routing key is still available in the received
		// message.
		String routingKey = "ourTestRoutingKey";

		// Argument processing: the first arg is the message, the rest are
		// key value pairs for headers.
		String message = args[0];

		// The map for the headers.
		Map<String, Object> headers = new HashMap<String, Object>();

		// The rest of the arguments are key value header pairs. For the purpose of this
		// example, we are assuming they are all strings, but that is not required by
		// RabbitMQ
		for (int i = 1; i < args.length; i++) {
			System.out.println("Adding header " + args[i] + " with value " + args[i + 1] + " to Map");
			headers.put(args[i], args[i + 1]);
			i++;
		}

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);

			AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();

			// MessageProperties.PERSISTENT_TEXT_PLAIN is a static instance of
			// AMQP.BasicProperties
			// that contains a delivery mode and a priority. So we pass them to the builder.
			builder.deliveryMode(MessageProperties.PERSISTENT_TEXT_PLAIN.getDeliveryMode());
			builder.priority(MessageProperties.PERSISTENT_TEXT_PLAIN.getPriority());

			// Add the headers to the builder.
			builder.headers(headers);

			// Use the builder to create the BasicProperties object.
			AMQP.BasicProperties theProps = builder.build();

			// Now we add the headers. This example only uses string headers, but they can
			// also be integers
			channel.basicPublish(EXCHANGE_NAME, routingKey, theProps, message.getBytes("UTF-8"));
			System.out.println(" [x] Sent message: '" + message + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
