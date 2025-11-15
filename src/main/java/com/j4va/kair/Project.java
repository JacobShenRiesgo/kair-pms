package com.j4va.kair;

import java.time.LocalDate;

/**
 * Simple data holder that matches your DB mapping in DatabaseService.getAllProjects()
 * Constructor and getters expected by controllers.
 */
public class Project {
    private final int id;
    private final String work;        // p.name
    private final String assignee;    // user email or "Unassigned"
    private final String priority;    // we default to "Medium"
    private final String status;      // project_status.status
    private final LocalDate createdDate; // p.start_date
    private final LocalDate dueDate;     // p.end_date

    public Project(int id, String work, String assignee, String priority,
                   String status, LocalDate createdDate, LocalDate dueDate) {
        this.id = id;
        this.work = work;
        this.assignee = assignee;
        this.priority = priority;
        this.status = status;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
    }

    // convenience constructor for new projects (id unknown)
    public Project(String work, String assignee, String priority, String status,
                   LocalDate createdDate, LocalDate dueDate) {
        this(0, work, assignee, priority, status, createdDate, dueDate);
    }

    public int getId() { return id; }
    public String getWork() { return work; }
    public String getAssignee() { return assignee; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public LocalDate getCreatedDate() { return createdDate; }
    public LocalDate getDueDate() { return dueDate; }
}