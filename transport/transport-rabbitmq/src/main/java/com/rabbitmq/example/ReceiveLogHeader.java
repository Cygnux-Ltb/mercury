package com.rabbitmq.example;

import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

public class ReceiveLogHeader {
	private static final String EXCHANGE_NAME = "header_test";

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Usage: ReceiveLogsHeader queueName [headers]...");
			System.exit(1);
		}

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);

			// The API requires a routing key, but in fact if you are using a header
			// exchange the
			// value of the routing key is not used in the routing. You can receive
			// information
			// from the sender here as the routing key is still available in the received
			// message.
			String routingKeyFromUser = "ourTestRoutingKey";

			// Argument processing: the first arg is the local queue name, the rest are
			// key value pairs for headers.
			String queueInputName = args[0];

			// The map for the headers.
			Map<String, Object> headers = new HashMap<>();

			// The rest of the arguments are key value header pairs. For the purpose of this
			// example, we are assuming they are all strings, but that is not required by
			// RabbitMQ
			// Note that when you run this code you should include the x-match header on the
			// command
			// line. Example:
			// java -cp $CP ReceiveLogsHeader testQueue1 x-match any header1 value1
			for (int i = 1; i < args.length; i++) {
				headers.put(args[i], args[i + 1]);
				System.out.println(
						"Binding header " + args[i] + " and value " + args[i + 1] + " to queue " + queueInputName);
				i++;
			}

			String queueName = channel.queueDeclare(queueInputName, true, false, false, null).getQueue();
			channel.queueBind(queueName, EXCHANGE_NAME, routingKeyFromUser, headers);

			System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
			};
			channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
