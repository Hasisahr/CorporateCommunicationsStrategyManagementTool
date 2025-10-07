package hr.javafx.project.csmt.exception;

/**
 * An unchecked runtime exception thrown when a user's entered password and confirmation do not match.
 * Commonly used during registration or credential update in the {@link hr.javafx.project.csmt.utils.PasswordUtils}.
 * Implements the main constructors from the {@link RuntimeException}
 * Typically caught to trigger UI-level feedback and stop further processing.
 *
 */
public class PasswordsNotMatchingException extends RuntimeException {
    public PasswordsNotMatchingException(String message) {
        super(message);
    }

    public PasswordsNotMatchingException() {
        super();
    }

    public PasswordsNotMatchingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordsNotMatchingException(Throwable cause) {
        super(cause);
    }

    protected PasswordsNotMatchingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
