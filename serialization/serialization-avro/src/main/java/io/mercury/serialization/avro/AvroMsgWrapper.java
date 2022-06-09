package io.mercury.serialization.avro;

import java.nio.ByteBuffer;

import io.mercury.common.codec.Envelope;
import io.mercury.common.sequence.EpochSequence;

public final class AvroMsgWrapper {

	private AvroMsgWrapper() {
	}

	/**
	 * 
	 * @param envelope
	 * @param content
	 * 
	 * @return AvroBinaryMsg
	 */
	public static AvroBinaryMsg wrap(io.mercury.serialization.avro.msg.Envelope envelope, ByteBuffer content) {
		return wrap(envelope, EpochSequence.allocate(), System.currentTimeMillis(), content);
	}

	/**
	 * 
	 * @param envelope
	 * @param sequence
	 * @param epoch
	 * @param content
	 * 
	 * @return AvroBinaryMsg
	 */
	public static AvroBinaryMsg wrap(io.mercury.serialization.avro.msg.Envelope envelope, long sequence, long epoch,
			ByteBuffer content) {
		return AvroBinaryMsg.newBuilder().setEnvelope(envelope).setSequence(sequence).setEpoch(epoch)
				.setContent(content).build();
	}

	/**
	 * 
	 * @param envelope
	 * @param content
	 * 
	 * @return AvroTextMsg
	 */
	public static AvroTextMsg wrap(io.mercury.serialization.avro.msg.Envelope envelope, String content) {
		return wrap(envelope, EpochSequence.allocate(), System.currentTimeMillis(), content);
	}

	/**
	 * 
	 * @param envelope
	 * @param sequence
	 * @param epoch
	 * @param content
	 * 
	 * @return AvroTextMsg
	 */
	public static AvroTextMsg wrap(io.mercury.serialization.avro.msg.Envelope envelope, long sequence, long epoch,
			String content) {
		return AvroTextMsg.newBuilder().setEnvelope(envelope).setSequence(sequence).setEpoch(epoch).setContent(content)
				.build();
	}

	/**
	 * 
	 * @param envelope
	 * @param contentType
	 * @param content
	 * 
	 * @return AvroBinaryMsg
	 */
	public static AvroBinaryMsg wrap(Envelope envelope, ContentType contentType, ByteBuffer content) {
		return wrap(envelope, contentType, EpochSequence.allocate(), System.currentTimeMillis(), content);
	}

	/**
	 * 
	 * @param envelope
	 * @param contentType
	 * @param sequence
	 * @param epoch
	 * @param content
	 * 
	 * @return AvroBinaryMsg
	 */
	public static AvroBinaryMsg wrap(Envelope envelope, ContentType contentType, long sequence, long epoch,
			ByteBuffer content) {
		return AvroBinaryMsg.newBuilder()
				.setEnvelope(io.mercury.serialization.avro.msg.Envelope.newBuilder().setCode(envelope.getCode())
						.setVersion(envelope.getVersion()).setContentType(contentType).build())
				.setSequence(sequence).setEpoch(epoch).setContent(content).build();
	}

	/**
	 * 
	 * @param envelope
	 * @param contentType
	 * @param content
	 * 
	 * @return AvroTextMsg
	 */
	public static AvroTextMsg wrap(Envelope envelope, ContentType contentType, String content) {
		return wrap(envelope, contentType, EpochSequence.allocate(), System.currentTimeMillis(), content);
	}

	/**
	 * 
	 * @param envelope
	 * @param contentType
	 * @param sequence
	 * @param epoch
	 * @param content
	 * 
	 * @return AvroTextMsg
	 */
	public static AvroTextMsg wrap(Envelope envelope, ContentType contentType, long sequence, long epoch,
			String content) {
		return AvroTextMsg.newBuilder()
				.setEnvelope(io.mercury.serialization.avro.msg.Envelope.newBuilder().setCode(envelope.getCode())
						.setVersion(envelope.getVersion()).setContentType(contentType).build())
				.setSequence(sequence).setEpoch(epoch).setContent(content).build();
	}

}
