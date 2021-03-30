package io.mercury.transport.rabbitmq;

import org.junit.Test;

import io.mercury.transport.rabbitmq.declare.ExchangeDefinition;

public class RabbitMqDeclareOperatorTest {

	@Test
	public void test() {
		
		ExchangeDefinition fanout = ExchangeDefinition.fanout("FAN_T1");

		System.out.println(fanout);
		
	}

}
