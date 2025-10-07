package hr.javafx.project.csmt.model;

import hr.javafx.project.csmt.enums.TaskCompletion;

import java.time.LocalDate;

/**
 * Represents a task entity which contains details such as name, description,
 * due date, completion status, creator, and the associated company.
 * Implements the {@link Priority} interface to calculate its priority and extends {@link Entity}
 * to inherit a unique identifier.
 * This class is used as a base for the application and it's workflow.
 *
 */

public class Task extends Entity implements Priority{
    String name;
    String description;
    LocalDate due;
    TaskCompletion completion;
    String createdBy;
    Long companyId;

    public Task(Long id, String name, String description, LocalDate due, TaskCompletion completion, String createdBy, Long companyId) {
        super(id);
        this.name = name;
        this.description = description;
        this.due = due;
        this.completion = completion;
        this.createdBy = createdBy;
        this.companyId = companyId;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDue() {
        return due;
    }

    public TaskCompletion getCompletion() {
        return completion;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Long getCompanyId() {
        return companyId;
    }

    @Override
    public String toString() {
        return "Task:" +
                name;
    }
}
