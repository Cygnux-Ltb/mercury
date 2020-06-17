package io.mercury.transport.rabbitmq;

import org.junit.Test;

import io.mercury.transport.rabbitmq.declare.ExchangeRelation;

public class RabbitMqDeclareDSLTest {

	@Test
	public void test() {
		
		ExchangeRelation.fanout("FAN_T1");
		
	}

}
