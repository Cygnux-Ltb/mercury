package io.mercury.serialization.json;

import io.mercury.common.epoch.EpochUnit;
import io.mercury.common.serialization.specific.JsonSerializable;
import io.mercury.common.serialization.ContentType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class JsonMsg implements JsonSerializable {

    private long sequence;
    private long epoch;
    private EpochUnit epochUnit = EpochUnit.MILLIS;
    private int envelope;
    private int version = 1;
    private ContentType contentType;
    private String content;

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

    public String getContent() {
        return content;
    }

    public JsonMsg setSequence(long sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public JsonMsg setEpochUnit(EpochUnit epochUnit) {
        this.epochUnit = epochUnit;
        return this;
    }

    public JsonMsg setEnvelope(int envelope) {
        this.envelope = envelope;
        return this;
    }

    public JsonMsg setVersion(int version) {
        this.version = version;
        return this;
    }

    public JsonMsg setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public JsonMsg setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return toJson();
    }

    @Nonnull
    @Override
    public String toJson() {
        return JsonWriter.toJson(this);
    }

    /**
     * @param json String
     * @return JsonMsg
     */
    @Nullable
    public static JsonMsg fromJson(String json) {
        return JsonParser.toObject(json, JsonMsg.class);
    }

}
