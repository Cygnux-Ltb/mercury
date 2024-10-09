package io.mercury.serialization.json;

import com.alibaba.fastjson2.JSONObject;
import io.mercury.common.epoch.EpochUnit;
import io.mercury.common.serialization.specific.JsonSerializable;

import javax.annotation.Nonnull;

public class JsonRecord implements JsonSerializable {

    private String title;
    private long epochTime;
    private EpochUnit epochUnit;
    private JSONObject record;

    public String getTitle() {
        return title;
    }

    public JsonRecord setTitle(String title) {
        this.title = title;
        return this;
    }

    public long getEpochTime() {
        return epochTime;
    }

    public JsonRecord setEpochTime(long epochTime) {
        this.epochTime = epochTime;
        return this;
    }

    public EpochUnit getEpochUnit() {
        return epochUnit;
    }

    public JsonRecord setEpochUnit(EpochUnit epochUnit) {
        this.epochUnit = epochUnit;
        return this;
    }

    public JSONObject getRecord() {
        return record;
    }

    public <T> T getRecordWith(Class<T> clazz) {
        return record.to(clazz);
    }

    public JsonRecord setRecord(Object record) {
        return setRecord(JSONObject.from(record));
    }

    public JsonRecord setRecord(JSONObject record) {
        this.record = record;
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

}
