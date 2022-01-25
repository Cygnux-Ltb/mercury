package io.mercury.transport.rmq;

import org.junit.Test;

import io.mercury.transport.rmq.declare.ExchangeRelationship;

public class RmqDeclareOperatorTest {

	@Test
	public void test() {

		ExchangeRelationship fanout = ExchangeRelationship.fanout("FAN_T1");

		System.out.println(fanout);

	}

}
