package hr.javafx.project.csmt.exception;
/**
 * A checked exception indicating that a requested database query returned no results.
 * Thrown when a data retrieval operation completes successfully but finds no matching records.
 * Helping distinguish the empty database set from the SQLException.
 * Mostly used to signal that the database contains no
 * tasks, messages, or entities were found.
 * Implements the main {@link Exception} constructors.
 *
 */
public class DatabaseEmptyException extends Exception {
    public DatabaseEmptyException(String message) {
        super(message);
    }

    public DatabaseEmptyException() {
        super();
    }

    public DatabaseEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseEmptyException(Throwable cause) {
        super(cause);
    }

    protected DatabaseEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
