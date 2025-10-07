package hr.javafx.project.csmt.controller;

import hr.javafx.project.csmt.LoginApplication;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Controller class for handling the logout action in the user interface.
 * Closes the current window and redirects the user back to the login screen.
 * This class is used in every scene across the application, and it logs the user out of their session.
 *
 */
public class LogoutButtonController {

    /**
     * Handles the logout action triggered by a UI event.
     * Closes the active JavaFX stage and displays the login screen.
     *
     * @param event the action event triggered by the logout button
     */
    public void logout(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        LoginApplication.showLoginScreen();
    }
}
