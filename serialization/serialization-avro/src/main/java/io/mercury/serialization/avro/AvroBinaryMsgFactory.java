package io.mercury.serialization.avro;

import java.nio.ByteBuffer;

import com.lmax.disruptor.EventFactory;

import io.mercury.serialization.avro.msg.AvroBinaryMsg;
import io.mercury.serialization.avro.msg.ContentType;
import io.mercury.serialization.avro.msg.Envelope;

/**
 * AvroBinaryMsgFactory implements EventFactory
 * 
 * @author yellow013
 *
 */
public class AvroBinaryMsgFactory implements EventFactory<AvroBinaryMsg> {

	public static final AvroBinaryMsgFactory INSTANCE = new AvroBinaryMsgFactory();

	@Override
	public AvroBinaryMsg newInstance() {
		return emptyBinaryMsg();
	}

	private static final ByteBuffer EMPTY_BUF = ByteBuffer.allocate(0);

	public static final Envelope emptyEnvelope() {
		return Envelope.newBuilder().setCode(Integer.MIN_VALUE).setContentType(ContentType.NULL)
				.setVersion(Integer.MIN_VALUE).build();
	}

	public static final AvroBinaryMsg emptyBinaryMsg() {
		return AvroBinaryMsg.newBuilder().setEnvelope(emptyEnvelope()).setEpoch(0).setSequence(0).setContent(EMPTY_BUF)
				.build();
	}

}
