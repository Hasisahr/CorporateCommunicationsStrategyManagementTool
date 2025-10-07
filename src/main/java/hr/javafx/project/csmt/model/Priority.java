package hr.javafx.project.csmt.model;

import hr.javafx.project.csmt.enums.TaskPriority;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Interface defining task prioritization logic based on due dates.
 * Provides a default method for determining the {@link TaskPriority}
 * level depending on how soon the task is due.
 */

public interface Priority {
    /**
     * Determines the priority of a task based on its due date.High Priority if due in fewer than 3 days,
     * low Priority if due in 10 or more days and a medium Priority otherwise.
     *
     * @param dateDue the due date of the task
     * @return the {@link TaskPriority} level
     */

    default TaskPriority getPriority(LocalDate dateDue) {
    long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), dateDue);
    TaskPriority priority = TaskPriority.MEDIUM_PRIORITY;
        if (daysBetween < 3) {
            priority = TaskPriority.HIGH_PRIORITY;
        }
        else if (daysBetween >= 10) {
            priority = TaskPriority.LOW_PRIORITY;
        }
        return priority;
    }
}
