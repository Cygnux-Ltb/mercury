package io.mercury.serialization.fury;

import io.mercury.common.epoch.EpochUnit;
import io.mercury.common.sequence.OrderedObject;
import io.mercury.common.serialization.ContentType;
import org.apache.fury.Fury;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class NotThreadSafeFuryMsg implements OrderedObject<NotThreadSafeFuryMsg> {

    private long sequence;
    private long epoch;
    private EpochUnit epochUnit = EpochUnit.MILLIS;
    private int envelope;
    private int version = 1;
    private ContentType contentType;
    private byte[] content;

    public long getSequence() {
        return sequence;
    }

    public NotThreadSafeFuryMsg setSequence(long sequence) {
        this.sequence = sequence;
        return this;
    }

    public long getEpoch() {
        return epoch;
    }

    public NotThreadSafeFuryMsg setEpoch(long epoch) {
        this.epoch = epoch;
        return this;
    }

    public EpochUnit getEpochUnit() {
        return epochUnit;
    }

    public NotThreadSafeFuryMsg setEpochUnit(EpochUnit epochUnit) {
        this.epochUnit = epochUnit;
        return this;
    }

    public int getEnvelope() {
        return envelope;
    }

    public NotThreadSafeFuryMsg setEnvelope(int envelope) {
        this.envelope = envelope;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public NotThreadSafeFuryMsg setVersion(int version) {
        this.version = version;
        return this;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public NotThreadSafeFuryMsg setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public byte[] getContent() {
        return content;
    }

    public NotThreadSafeFuryMsg setContent(byte[] content) {
        this.content = content;
        return this;
    }

    @Override
    public long orderNum() {
        return sequence;
    }

    @Nonnull
    public NotThreadSafeFuryMsg fromBytes(@Nonnull Fury fury, @Nonnull byte[] bytes) {
        return fury.deserializeJavaObject(bytes, NotThreadSafeFuryMsg.class);
    }

    @Nonnull
    public byte[] toBytes(@Nonnull Fury fury) {
        return fury.serializeJavaObject(this);
    }


}
