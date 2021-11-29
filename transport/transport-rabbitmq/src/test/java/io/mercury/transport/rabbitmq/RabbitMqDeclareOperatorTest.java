package io.mercury.transport.rabbitmq;

import org.junit.Test;

import io.mercury.transport.rabbitmq.declare.ExchangeDef;

public class RabbitMqDeclareOperatorTest {

	@Test
	public void test() {
		
		ExchangeDef fanout = ExchangeDef.fanout("FAN_T1");

		System.out.println(fanout);
		
	}

}
