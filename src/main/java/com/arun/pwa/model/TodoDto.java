package com.arun.pwa.model;

public class TodoDto {

    private String id;          // Airtable recordId
    private String title;
    private boolean completed;
    private String category;
    private String priority;
    private String createdAt;
    private String completedAt;

    // getters and setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        return "TodoDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                ", category='" + category + '\'' +
                ", priority='" + priority + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", completedAt='" + completedAt + '\'' +
                '}';
    }
}
