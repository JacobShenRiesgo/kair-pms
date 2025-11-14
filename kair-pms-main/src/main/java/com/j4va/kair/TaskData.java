package com.j4va.kair;

import java.util.UUID;

/**
 * Stores the data of a task card such as title, description, priority, and status.
 */
public class TaskData {

    private final String id = UUID.randomUUID().toString(); // Unique ID for the task
    private String title;
    private String description;
    private String priority;
    private String status;


    /**
     * Constructor for creating a new TaskData object.
     *
     * @param title       The title of the task.
     * @param description The description of the task.
     * @param priority    The priority level of the task.
     * @param status      The current status of the task (e.g., "To Do", "In Progress", "Done").
     */
    public TaskData(String title, String description, String priority, String status) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
