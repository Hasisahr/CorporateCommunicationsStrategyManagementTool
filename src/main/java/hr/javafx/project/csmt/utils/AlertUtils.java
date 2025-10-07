package hr.javafx.project.csmt.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Utility class for displaying JavaFX alert dialogs in different interface controllers across the application.
 * Provides static methods for showing confirmation, error, and success messages.
 * Not an instantiable class, used only as a utility.
 *
 */


public class AlertUtils {
    private AlertUtils() {}

    /**
     * Displays a confirmation dialog with the provided messages and title.
     * If the user confirms, a success alert is also displayed.
     *
     * @param message the base confirmation message
     * @param objectMessage the name or description of the object being acted on
     * @param title the title to display in the confirmation dialog
     * @return {@code true} if the user confirmed (clicked OK), {@code false} otherwise
     */
    public static Boolean showConfirmationAlert(String message, String objectMessage, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please confirm" + title);
        alert.setContentText(message + " " + objectMessage + " ?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            showSuccessAlert(objectMessage);
        }
        return alert.getResult() == ButtonType.OK;
    }

    /**
     * Displays an error alert dialog with a header and detailed message.
     *
     * @param header the header text of the error dialog
     * @param errors the main message or error details to display
     */
    public static void showErrorAlert(String header, String errors) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(errors);
        alert.showAndWait();
    }


    /**
     * Displays a success information dialog with a success message.
     *
     * @param message additional context to show in the success alert
     */
    public static void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Action performed successfully");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
