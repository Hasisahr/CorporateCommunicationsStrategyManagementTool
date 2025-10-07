package hr.javafx.project.csmt.controller.login;

import hr.javafx.project.csmt.utils.ShowScreenUtils;

/**
 * Controller class that handles navigation back to the login screen.
 * Intended for use in screens during authentication or registration to go back to the login screen.
 * Attached to a "Back to Log in" button in registration screens.
 *
 */

public class BackToLoginButtonController {

    /**
     * Loads and displays the login screen using the screen utility.
     * Calls {@link ShowScreenUtils#showScreen(String, int, int)} to load the login FXML file.
     */
    public void backToLoginScreen(){
        ShowScreenUtils showScreenUtils = new ShowScreenUtils();
        showScreenUtils.showScreen("loginScreen.fxml", 400, 600);
    }
}
