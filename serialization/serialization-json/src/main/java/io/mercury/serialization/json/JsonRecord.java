package io.mercury.serialization.json;

public class JsonRecord {

    private String title;
    private Object record;

    public String getTitle() {
        return title;
    }

    public Object getRecord() {
        return record;
    }

    public JsonRecord setTitle(String title) {
        this.title = title;
        return this;
    }

    public JsonRecord setRecord(Object record) {
        this.record = record;
        return this;
    }

}
