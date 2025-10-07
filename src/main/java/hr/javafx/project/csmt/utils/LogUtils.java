package hr.javafx.project.csmt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for logging throughout the application
 * without having to instantiate a Logger across the application.
 * Provides static methods to log informational and error messages
 * using SLF4J's {@link Logger} interface.
 * This class is non-instantiable and is mainly used as a utility.
 *
 */


public class LogUtils {
    private LogUtils() {}

    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    /**
     * Logs an informational message at the INFO level.
     *
     * @param message the message to log
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Logs an error message at the ERROR level.
     *
     * @param message the message to log
     */
    public static void error(String message) {
        logger.error(message);
    }
}
