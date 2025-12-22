package com.arun.pwa.model.airtable;

import java.util.List;

public class AirtableResponse {

    private List<AirtableRecord> records;

    public List<AirtableRecord> getRecords() {
        return records;
    }

    public void setRecords(List<AirtableRecord> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "AirtableResponse{" +
                "records=" + (records != null ? records.toString() : "null") +
                '}';
    }
}
