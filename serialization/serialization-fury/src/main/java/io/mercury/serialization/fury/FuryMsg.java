package io.mercury.serialization.fury;

import io.mercury.common.epoch.EpochUnit;
import io.mercury.common.sequence.SerialObject;
import io.mercury.common.serialization.ContentType;
import io.mercury.common.serialization.specific.BytesDeserializable;
import io.mercury.common.serialization.specific.BytesSerializable;
import org.apache.fury.Fury;
import org.apache.fury.ThreadLocalFury;
import org.apache.fury.ThreadSafeFury;
import org.apache.fury.config.Language;

import javax.annotation.Nonnull;


public final class FuryMsg implements SerialObject<FuryMsg>, BytesSerializable, BytesDeserializable<FuryMsg> {

    private static final ThreadSafeFury THREAD_SAFE_FURY = new ThreadLocalFury(classLoader -> {
        Fury fury = Fury.builder()
                .withLanguage(Language.JAVA)
                .withClassLoader(classLoader)
                .build();
        fury.register(FuryMsg.class);
        return fury;
    });


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

    public long getEpoch() {
        return epoch;
    }

    public EpochUnit getEpochUnit() {
        return epochUnit;
    }

    public int getEnvelope() {
        return envelope;
    }

    public int getVersion() {
        return version;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public FuryMsg setSequence(long sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public FuryMsg setEpochUnit(EpochUnit epochUnit) {
        this.epochUnit = epochUnit;
        return this;
    }

    public FuryMsg setEnvelope(int envelope) {
        this.envelope = envelope;
        return this;
    }

    public FuryMsg setVersion(int version) {
        this.version = version;
        return this;
    }

    public FuryMsg setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
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
        return THREAD_SAFE_FURY.deserializeJavaObject(bytes, FuryMsg.class);
    }

    @Nonnull
    @Override
    public byte[] toBytes() {
        return THREAD_SAFE_FURY.serializeJavaObject(this);
    }
}
