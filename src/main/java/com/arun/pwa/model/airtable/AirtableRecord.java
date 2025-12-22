package com.arun.pwa.model.airtable;

public class AirtableRecord {

    private String id;
    private AirtableFields fields;

    public String getId() {
        return id;
    }

    public AirtableFields getFields() {
        return fields;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFields(AirtableFields fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "AirtableRecord{" +
                "id='" + id + '\'' +
                ", fields=" + (fields != null ? fields.toString() : "null") +
                '}';
    }
}
