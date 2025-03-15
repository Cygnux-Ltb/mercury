package io.mercury.serialization.fury;

import io.mercury.common.epoch.EpochUnit;
import io.mercury.common.sequence.OrderedObject;
import io.mercury.common.serialization.ContentType;
import org.apache.fury.Fury;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class FuryMsg implements OrderedObject<FuryMsg> {

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

    public FuryMsg setSequence(long sequence) {
        this.sequence = sequence;
        return this;
    }

    public long getEpoch() {
        return epoch;
    }

    public FuryMsg setEpoch(long epoch) {
        this.epoch = epoch;
        return this;
    }

    public FuryMsg setEpoch(long epoch, EpochUnit epochUnit) {
        this.epoch = epoch;
        this.epochUnit = epochUnit;
        return this;
    }

    public EpochUnit getEpochUnit() {
        return epochUnit;
    }

    public FuryMsg setEpochUnit(EpochUnit epochUnit) {
        this.epochUnit = epochUnit;
        return this;
    }

    public int getEnvelope() {
        return envelope;
    }

    public FuryMsg setEnvelope(int envelope) {
        this.envelope = envelope;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public FuryMsg setVersion(int version) {
        this.version = version;
        return this;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public FuryMsg setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public byte[] getContent() {
        return content;
    }

    public FuryMsg setContent(byte[] content) {
        this.content = content;
        return this;
    }

    @Override
    public long orderNum() {
        return sequence;
    }

    @Nonnull
    public byte[] toBytes(@Nonnull Fury fury) {
        return fury.serializeJavaObject(this);
    }

    @Nonnull
    public static FuryMsg fromBytes(@Nonnull Fury fury, @Nonnull byte[] bytes) {
        return fury.deserializeJavaObject(bytes, FuryMsg.class);
    }

}
