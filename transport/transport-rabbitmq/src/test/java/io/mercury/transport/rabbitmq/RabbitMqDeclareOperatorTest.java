package io.mercury.transport.rabbitmq;

import org.junit.Test;

import io.mercury.transport.rabbitmq.declare.ExchangeRelationship;

public class RabbitMqDeclareOperatorTest {

	@Test
	public void test() {
		
		ExchangeRelationship fanout = ExchangeRelationship.fanout("FAN_T1");

		System.out.println(fanout);
		
	}

}
