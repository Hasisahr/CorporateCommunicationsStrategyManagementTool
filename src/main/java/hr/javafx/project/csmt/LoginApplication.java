package hr.javafx.project.csmt;

import hr.javafx.project.csmt.utils.LogUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX application class responsible for launching the login interface.
 * Initializes and registers the primary stage, and sets up the login screen
 * defined in {@code loginScreen.fxml}.
 * This application serves as the entry for the application's GUI.
 *
 */
public class LoginApplication extends Application {

    private static Stage primaryStage;
    public static final int MAIN_SCREEN_WIDTH = 793;
    public static final int MAIN_SCREEN_HEIGHT = 615;


    /**
     * JavaFX application entry point. Registers and launches the login screen.
     *
     * @param stage the primary stage provided by the JavaFX framework
     */
    @Override
    public void start(Stage stage) {
        registerPrimaryStage(stage);
        showLoginScreen();
    }

    /**
     * A static method used for registering the primary stage so it can be accessed statically across the application.
     *
     * @param stage the JavaFX primary stage to store
     */
    private static void registerPrimaryStage(Stage stage){
        primaryStage = stage;
    }


    /**
     * Loads the login screen from the FXML file and displays it
     * in the primary application window.
     */
    public static void showLoginScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("loginScreen.fxml"));
            Parent root = fxmlLoader.load();
            primaryStage.setTitle("Corporate communications strategy management tool");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }
    }



    public static void main(String[] args) {
        launch();
    }

    /**
     * Returns the main JavaFX stage instance used for displaying different screens throughout the application.
     *
     * @return the primary stage
     */
    public static Stage getStage() {
        return primaryStage;
    }
}