package hr.javafx.project.csmt.utils;

import hr.javafx.project.csmt.LoginApplication;
import hr.javafx.project.csmt.controller.manager.ProjectManagerMainScreenController;
import hr.javafx.project.csmt.controller.member.TeamMemberMainScreenController;
import hr.javafx.project.csmt.model.ProjectManager;
import hr.javafx.project.csmt.model.TeamMember;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

import static hr.javafx.project.csmt.LoginApplication.MAIN_SCREEN_HEIGHT;
import static hr.javafx.project.csmt.LoginApplication.MAIN_SCREEN_WIDTH;


/**
 * Utility class for loading and displaying different JavaFX FXML scenes.
 * Provides centralized methods for switching views based on application state,
 * including the login screen, project manager screen, and team member screen.
 * Uses the getStage() method from {@link LoginApplication} to update the visible scene
 * and provides controller injection where needed.
 *
 */
public class ShowScreenUtils {


    /**
     * Creates an {@link FXMLLoader} for the specified FXML file.
     *
     * @param fxmlFileName the path to the FXML file
     * @return an FXMLLoader instance
     */
    public FXMLLoader loader(String fxmlFileName){
        return new FXMLLoader(LoginApplication.class.getResource(fxmlFileName));
    }

    /**
     * Loads and displays a screen from the given FXML file with provided dimensions.
     *
     * @param fxmlFileName the file to load (e.g. "loginScreen.fxml")
     * @param height height of the window
     * @param width width of the window
     */
    public void showScreen(String fxmlFileName, int height, int width) {

        Scene scene = null;
        try {
            scene = new Scene(loader(fxmlFileName).load(), width, height);
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }

        LoginApplication.getStage().setScene(scene);
        LoginApplication.getStage().show();
    }

    /**
     * Loads and displays the project manager screen
     * and sets the {@link ProjectManager} in the controller.
     *
     * @param projectManager the logged-in project manager to pass into the next controller
     */
    public void showProjectManagerScreen(ProjectManager projectManager) {
        FXMLLoader loader = loader("projectManagerScreen.fxml");
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), MAIN_SCREEN_WIDTH, MAIN_SCREEN_HEIGHT);
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }

        ProjectManagerMainScreenController projectManagerMainScreenController = loader.getController();
        projectManagerMainScreenController.setProjectManager(projectManager);

        LoginApplication.getStage().setScene(scene);
        LoginApplication.getStage().show();
    }

    /**
     * Loads and displays the team member screen
     * and sets the {@link TeamMember} in the controller.
     *
     * @param teamMember the logged-in team member to pass into the next controller
     */
    public void showTeamMemberScreen(TeamMember teamMember) {
        FXMLLoader loader = loader("teamMemberMainScreen.fxml");
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), MAIN_SCREEN_WIDTH, MAIN_SCREEN_HEIGHT);
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }

        TeamMemberMainScreenController teamMemberMainScreenController = loader.getController();
        teamMemberMainScreenController.setTeamMember(teamMember);

        LoginApplication.getStage().setScene(scene);
        LoginApplication.getStage().show();
    }
}

