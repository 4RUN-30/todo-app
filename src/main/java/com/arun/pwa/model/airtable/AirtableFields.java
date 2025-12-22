package com.arun.pwa.model.airtable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AirtableFields {

    private String title;
    private boolean completed;
    private String category;

    @JsonProperty("Priority")
    private String priority;

    private String createdAt;
    private String completedAt;

    // getters & setters

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getCategory() {
        return category;
    }

    public String getPriority() {
        return priority;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        return "AirtableFields{" +
                "title='" + title + '\'' +
                ", completed=" + completed +
                ", category='" + category + '\'' +
                ", priority='" + priority + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", completedAt='" + completedAt + '\'' +
                '}';
    }
}
