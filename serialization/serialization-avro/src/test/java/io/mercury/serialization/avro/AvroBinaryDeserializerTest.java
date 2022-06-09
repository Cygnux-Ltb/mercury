package io.mercury.serialization.avro;

import java.nio.ByteBuffer;

import org.junit.Test;

import io.mercury.common.datetime.EpochTime;

public class AvroBinaryDeserializerTest {

	@Test
	public void test() {
		AvroBinarySerializer<AvroBinaryMsg> serializer = new AvroBinarySerializer<>(AvroBinaryMsg.class);
		AvroBinaryDeserializer<AvroBinaryMsg> deserializer = new AvroBinaryDeserializer<>(AvroBinaryMsg.class);

		AvroBinaryMsg msg0 = AvroBinaryMsgFactory.emptyBinaryMsg();
		msg0.getEnvelope().setCode(1).setContentType(ContentType.INT).setVersion(1);
		msg0.setEpoch(EpochTime.getEpochMillis()).setSequence(1).setContent(ByteBuffer.allocate(10));

		ByteBuffer buffer = serializer.serialization(msg0);

		msg0.setEpoch(EpochTime.getEpochMillis() + 1000);
		System.out.println(msg0);

		AvroBinaryMsg msg = deserializer.deserialization(buffer.array());

		System.out.println(msg);

	}

}
