package io.mercury.transport.rmq;

import io.mercury.transport.rmq.declare.ExchangeRelationship;
import org.junit.Test;

public class RmqDeclareOperatorTest {

	@Test
	public void test() {

		ExchangeRelationship fanout = ExchangeRelationship.fanout("FAN_T1");

		System.out.println(fanout);

	}

}
