package com.j4va.kair;

import java.util.UUID;

//Stores the data of the task cards such as title, description, etc.
public class TaskData {
    private final String id = UUID.randomUUID().toString();
    private String title;
    private String description;

    public TaskData(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}
