package io.mercury.serialization.fury;

import io.mercury.common.epoch.EpochUnit;
import io.mercury.common.sequence.OrderedObject;
import io.mercury.common.serialization.ContentType;
import io.mercury.common.serialization.specific.BytesDeserializable;
import io.mercury.common.serialization.specific.BytesSerializable;
import org.apache.fury.Fury;
import org.apache.fury.ThreadLocalFury;
import org.apache.fury.ThreadSafeFury;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import static org.apache.fury.config.Language.JAVA;

/**
 *
 */
@ThreadSafe
public final class ThreadSafeFuryMsg implements OrderedObject<ThreadSafeFuryMsg>,
        BytesSerializable, BytesDeserializable<ThreadSafeFuryMsg> {

    private static final ThreadSafeFury THREAD_SAFE_FURY = new ThreadLocalFury(ThreadSafeFuryMsg::newThreadLocalFury);

    private static Fury newThreadLocalFury(ClassLoader classLoader) {
        var fury = Fury.builder()
                .withLanguage(JAVA)
                .withClassLoader(classLoader)
                .build();
        fury.register(ThreadSafeFuryMsg.class);
        return fury;
    }

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

    public ThreadSafeFuryMsg setSequence(long sequence) {
        this.sequence = sequence;
        return this;
    }

    public long getEpoch() {
        return epoch;
    }

    public ThreadSafeFuryMsg setEpoch(long epoch) {
        this.epoch = epoch;
        return this;
    }

    public EpochUnit getEpochUnit() {
        return epochUnit;
    }

    public ThreadSafeFuryMsg setEpochUnit(EpochUnit epochUnit) {
        this.epochUnit = epochUnit;
        return this;
    }

    public int getEnvelope() {
        return envelope;
    }

    public ThreadSafeFuryMsg setEnvelope(int envelope) {
        this.envelope = envelope;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public ThreadSafeFuryMsg setVersion(int version) {
        this.version = version;
        return this;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public ThreadSafeFuryMsg setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public byte[] getContent() {
        return content;
    }

    public ThreadSafeFuryMsg setContent(byte[] content) {
        this.content = content;
        return this;
    }

    @Override
    public long orderNum() {
        return sequence;
    }

    @Nonnull
    @Override
    public ThreadSafeFuryMsg fromBytes(@Nonnull byte[] bytes) {
        return THREAD_SAFE_FURY.deserializeJavaObject(bytes, ThreadSafeFuryMsg.class);
    }

    @Nonnull
    @Override
    public byte[] toBytes() {
        return THREAD_SAFE_FURY.serializeJavaObject(this);
    }

}
