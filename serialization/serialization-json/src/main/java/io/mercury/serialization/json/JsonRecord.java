package io.mercury.serialization.json;

import io.mercury.common.epoch.EpochUnit;

public class JsonRecord {

    private String title;
    private long epochTime;
    private EpochUnit epochUnit;
    private Object record;

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

    public Object getRecord() {
        return record;
    }

    public JsonRecord setRecord(Object record) {
        this.record = record;
        return this;
    }
    
}
