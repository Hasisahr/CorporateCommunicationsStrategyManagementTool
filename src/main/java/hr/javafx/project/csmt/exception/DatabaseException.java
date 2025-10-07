package hr.javafx.project.csmt.exception;

/**
 * An unchecked runtime exception indicating a failure related to database access or operations.
 * Used for wrapping SQLExceptions and signal unexpected issues
 * during data insertion, retrieval, or connection handling.
 * Implements the main constructors from the {@link RuntimeException}
 *
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException() {
        super();
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }

    protected DatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
