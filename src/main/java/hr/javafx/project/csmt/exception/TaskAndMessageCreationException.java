package hr.javafx.project.csmt.exception;

/**
 * An unchecked runtime exception thrown when a task or message creation process fails.
 * Commonly used in forms or controllers to represent validation errors,
 * database issues, or unexpected interruptions during message or task creation.
 * Encapsulate creation-specific errors distinct from other runtime problems.
 * Implements the main constructors from the {@link RuntimeException}
 *
 */
public class TaskAndMessageCreationException extends RuntimeException {
    public TaskAndMessageCreationException() {
        super();
    }

    public TaskAndMessageCreationException(String message) {
        super(message);
    }

    public TaskAndMessageCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskAndMessageCreationException(Throwable cause) {
        super(cause);
    }

    protected TaskAndMessageCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
