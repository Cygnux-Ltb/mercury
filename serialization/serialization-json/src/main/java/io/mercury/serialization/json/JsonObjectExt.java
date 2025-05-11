package io.mercury.serialization.json;

import com.alibaba.fastjson2.JSONObject;
import io.mercury.common.epoch.EpochUnit;
import io.mercury.common.serialization.specific.JsonSerializable;

import javax.annotation.Nonnull;

public class JsonObjectExt implements JsonSerializable {

    private String title;
    private long epochTime;
    private EpochUnit epochUnit;
    private JSONObject object;

    public String getTitle() {
        return title;
    }

    public JsonObjectExt setTitle(String title) {
        this.title = title;
        return this;
    }

    public long getEpochTime() {
        return epochTime;
    }

    public JsonObjectExt setEpochTime(long epochTime) {
        this.epochTime = epochTime;
        return this;
    }

    public EpochUnit getEpochUnit() {
        return epochUnit;
    }

    public JsonObjectExt setEpochUnit(EpochUnit epochUnit) {
        this.epochUnit = epochUnit;
        return this;
    }

    public JSONObject getObject() {
        return object;
    }

    public <T> T getWith(Class<T> clazz) {
        return object.to(clazz);
    }

    public JsonObjectExt setObject(Object object) {
        return setObject(JSONObject.from(object));
    }

    public JsonObjectExt setObject(JSONObject object) {
        this.object = object;
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
