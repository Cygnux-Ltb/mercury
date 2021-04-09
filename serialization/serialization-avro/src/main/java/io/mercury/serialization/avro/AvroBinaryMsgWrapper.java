package io.mercury.serialization.avro;

import java.nio.ByteBuffer;

import io.mercury.common.codec.Envelope;
import io.mercury.common.sequence.EpochSequence;
import io.mercury.serialization.avro.msg.AvroBinaryMsg;
import io.mercury.serialization.avro.msg.ContentType;
import io.mercury.serialization.avro.msg.Envelope.Builder;

public final class AvroBinaryMsgWrapper {

	private AvroBinaryMsgWrapper() {
	}

	public static AvroBinaryMsg wrap(Envelope envelope) {
		Builder eb = io.mercury.serialization.avro.msg.Envelope.newBuilder();
		eb.setCode(envelope.getCode());
		eb.setVersion(0);
		eb.setContentType(ContentType.OBJECT);
		return AvroBinaryMsg.newBuilder().setEnvelope(eb.build()).setEpoch(System.currentTimeMillis())
				.setSequence(EpochSequence.allocate()).build();
	}

	public static AvroBinaryMsg wrap(Envelope envelope, int version, ContentType contentType, long sequence, long epoch,
			ByteBuffer bytes) {
		return AvroBinaryMsg.newBuilder()
				.setEnvelope(io.mercury.serialization.avro.msg.Envelope.newBuilder().setCode(envelope.getCode())
						.setVersion(version).setContentType(contentType).build())
				.setSequence(sequence).setEpoch(epoch).setContent(bytes).build();
	}

}
