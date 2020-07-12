package io.mercury.actors.reference;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.mercury.actors.messages.AvroMsg;

public abstract class AvroBinaryActor extends GenericActorT1<ByteBuffer> {

	@Override
	protected Class<ByteBuffer> eventType() {
		return ByteBuffer.class;
	}

	@Override
	protected final void onEvent(ByteBuffer binary) {
		try {
			AvroMsg msg = AvroMsg.fromByteBuffer(binary);
			onAvroMsg(msg);
		} catch (IOException e) {
			log.error("ByteBuffer deserialization throw IOException, message==[{}], binary.capacity==[{}]",
					e.getMessage(), binary.capacity(), e);
		}
	}

	protected abstract void onAvroMsg(AvroMsg msg);

}
