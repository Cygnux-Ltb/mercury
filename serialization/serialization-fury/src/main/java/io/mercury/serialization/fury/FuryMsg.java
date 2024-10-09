package io.mercury.serialization.fury;

import io.mercury.common.epoch.EpochUnit;
import io.mercury.common.sequence.SerialObject;
import io.mercury.common.serialization.ContentType;
import io.mercury.common.serialization.specific.BytesDeserializable;
import io.mercury.common.serialization.specific.BytesSerializable;

import javax.annotation.Nonnull;

/**
 *
 */
public final class FuryMsg implements SerialObject<FuryMsg>,
        BytesSerializable, BytesDeserializable<FuryMsg> {

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
    public long serialId() {
        return sequence;
    }

    @Nonnull
    @Override
    public FuryMsg fromBytes(@Nonnull byte[] bytes) {
        return FuryKeeper.FURY_MSG_USED.deserializeJavaObject(bytes, FuryMsg.class);
    }

    @Nonnull
    @Override
    public byte[] toBytes() {
        return FuryKeeper.FURY_MSG_USED.serializeJavaObject(this);
    }

}
