package com.j4va.kair;

import java.util.UUID;

// Stores the data of the task cards such as title, description, etc.
public class TaskData {
    private final String id = UUID.randomUUID().toString();
    private String title;
    private String description;
    private String status;

    public TaskData(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status; // correctly assign the constructor parameter
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
}
