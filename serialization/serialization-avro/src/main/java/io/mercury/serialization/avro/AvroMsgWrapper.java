package io.mercury.serialization.avro;

import io.mercury.common.sequence.EpochSequence;
import io.mercury.serialization.avro.msg.AvroBinaryMsg;
import io.mercury.serialization.avro.msg.AvroTextMsg;
import io.mercury.serialization.avro.msg.ContentType;

import java.nio.ByteBuffer;

public final class AvroMsgWrapper {

    private AvroMsgWrapper() {
    }

    /**
     * @param envelope io.mercury.serialization.avro.msg.Envelope
     * @param content  ByteBuffer
     * @return AvroBinaryMsg
     */
    public static AvroBinaryMsg wrap(io.mercury.serialization.avro.msg.Envelope envelope,
                                     ByteBuffer content) {
        return wrap(envelope, EpochSequence.allocate(), System.currentTimeMillis(), content);
    }

    /**
     * @param envelope io.mercury.serialization.avro.msg.Envelope
     * @param sequence long
     * @param epoch    long
     * @param content  ByteBuffer
     * @return AvroBinaryMsg
     */
    public static AvroBinaryMsg wrap(io.mercury.serialization.avro.msg.Envelope envelope,
                                     long sequence, long epoch, ByteBuffer content) {
        return AvroBinaryMsg.newBuilder().setEnvelope(envelope).setSequence(sequence).setEpoch(epoch)
                .setContent(content).build();
    }

    /**
     * @param envelope io.mercury.serialization.avro.msg.Envelope
     * @param content  String
     * @return AvroTextMsg
     */
    public static AvroTextMsg wrap(io.mercury.serialization.avro.msg.Envelope envelope,
                                   String content) {
        return wrap(envelope, EpochSequence.allocate(), System.currentTimeMillis(), content);
    }

    /**
     * @param envelope io.mercury.serialization.avro.msg.Envelope
     * @param sequence long
     * @param epoch    long
     * @param content  String
     * @return AvroTextMsg
     */
    public static AvroTextMsg wrap(io.mercury.serialization.avro.msg.Envelope envelope,
                                   long sequence, long epoch, String content) {
        return AvroTextMsg.newBuilder().setEnvelope(envelope).setSequence(sequence).setEpoch(epoch).setContent(content)
                .build();
    }

    /**
     * @param envelope    io.mercury.common.codec.Envelope
     * @param contentType ContentType
     * @param content     ByteBuffer
     * @return AvroBinaryMsg
     */
    public static AvroBinaryMsg wrap(io.mercury.common.codec.Envelope envelope,
                                     ContentType contentType, ByteBuffer content) {
        return wrap(envelope, contentType, EpochSequence.allocate(), System.currentTimeMillis(), content);
    }

    /**
     * @param envelope    io.mercury.common.codec.Envelope
     * @param contentType ContentType
     * @param sequence    long
     * @param epoch       long
     * @param content     ByteBuffer
     * @return AvroBinaryMsg
     */
    public static AvroBinaryMsg wrap(io.mercury.common.codec.Envelope envelope,
                                     ContentType contentType, long sequence, long epoch,
                                     ByteBuffer content) {
        return AvroBinaryMsg.newBuilder()
                .setEnvelope(io.mercury.serialization.avro.msg.Envelope.newBuilder().setCode(envelope.getCode())
                        .setVersion(envelope.getVersion()).setContentType(contentType).build())
                .setSequence(sequence).setEpoch(epoch).setContent(content).build();
    }

    /**
     * @param envelope    io.mercury.common.codec.Envelope
     * @param contentType ContentType
     * @param content     String
     * @return AvroTextMsg
     */
    public static AvroTextMsg wrap(io.mercury.common.codec.Envelope envelope,
                                   ContentType contentType, String content) {
        return wrap(envelope, contentType, EpochSequence.allocate(), System.currentTimeMillis(), content);
    }

    /**
     * @param envelope    io.mercury.common.codec.Envelope
     * @param contentType ContentType
     * @param sequence    long
     * @param epoch       long
     * @param content     String
     * @return AvroTextMsg
     */
    public static AvroTextMsg wrap(io.mercury.common.codec.Envelope envelope,
                                   ContentType contentType, long sequence, long epoch,
                                   String content) {
        return AvroTextMsg.newBuilder()
                .setEnvelope(io.mercury.serialization.avro.msg.Envelope.newBuilder().setCode(envelope.getCode())
                        .setVersion(envelope.getVersion()).setContentType(contentType).build())
                .setSequence(sequence).setEpoch(epoch).setContent(content).build();
    }

}
