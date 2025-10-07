package hr.javafx.project.csmt.enums;

/**
 * Enum representing the possible states of a task throughout its stages.
 * Used to track and represent task status:{@code WAITING_ASSIGNMENT} – the task has not been assigned yet
 {@code IN_PROGRESS} – the task is actively being worked on, {@code COMPLETED} – the task has been finished
 *
 */

public enum TaskCompletion {
    WAITING_ASSIGNMENT, IN_PROGRESS, COMPLETED
}
