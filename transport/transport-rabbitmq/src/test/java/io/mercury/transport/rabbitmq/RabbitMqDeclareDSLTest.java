package io.mercury.transport.rabbitmq;

import org.junit.Test;

import io.mercury.transport.rabbitmq.declare.ExchangeRelationship;

public class RabbitMqDeclareDSLTest {

	@Test
	public void test() {
		
		ExchangeRelationship.fanout("FAN_T1");
		
	}

}
