package hr.javafx.project.csmt.enums;

/**
 * Enum representing the urgency level of a task.
 * <p>
 * Used to prioritize tasks based on their due dates or importance:
 *   {@code LOW_PRIORITY} – minimal urgency
 *   {@code MEDIUM_PRIORITY} – moderate urgency
 *   {@code HIGH_PRIORITY} – requires immediate attention
 *   Mainly used in {@link hr.javafx.project.csmt.model.Priority} interface as a return
 *   for the priority of the task.
 *
 */

public enum TaskPriority {
    LOW_PRIORITY,MEDIUM_PRIORITY,HIGH_PRIORITY
}
