package hr.javafx.project.csmt.exception;

/**
 * A checked exception class for handling errors related to message operations.
 * Thrown when issues occur during message retrieval, creation, or insertion.
 * Implements the main {@link Exception} constructors.
 *
 */
public class MessageException extends Exception {
    public MessageException(String message) {
        super(message);
    }

  public MessageException() {
    super();
  }

  public MessageException(String message, Throwable cause) {
    super(message, cause);
  }

  public MessageException(Throwable cause) {
    super(cause);
  }

  protected MessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
