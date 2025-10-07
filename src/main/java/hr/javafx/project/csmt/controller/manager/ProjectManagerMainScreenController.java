package hr.javafx.project.csmt.controller.manager;

import hr.javafx.project.csmt.LoginApplication;
import hr.javafx.project.csmt.model.ProjectManager;
import hr.javafx.project.csmt.utils.LogUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

import static hr.javafx.project.csmt.LoginApplication.MAIN_SCREEN_HEIGHT;
import static hr.javafx.project.csmt.LoginApplication.MAIN_SCREEN_WIDTH;

/**
 * Controller for the main screen for the {@link ProjectManager}.
 * Offers navigation options to key task management views, including:
 * task creation, message creation, task assignment, and an overview
 * of all tasks tied to the manager’s company.
 * This screen acts as a central screen for the project manager’s responsibilities
 * and is launched after successful login.
 *
 */
public class ProjectManagerMainScreenController {

        ProjectManager projectManager;

        @FXML
        Label welcomeLabel;
    /**
     * Sets the currently logged-in {@link ProjectManager}.
     *
     * @param projectManager the current project manager logged in
     */
        public void setProjectManager(ProjectManager projectManager) {
            this.projectManager = projectManager;
        }

    /**
     * Navigates to the task creation screen and passes the manager object to the new screen.
     */
    public void showCreateTaskScreen(){
            FXMLLoader loader = new FXMLLoader(LoginApplication.class.getResource("createTask.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(loader.load(), MAIN_SCREEN_WIDTH, MAIN_SCREEN_HEIGHT);
            } catch (IOException e) {
                LogUtils.error(e.getMessage());
            }

            CreateTaskController createTaskController = loader.getController();
            createTaskController.setManager(projectManager);

            LoginApplication.getStage().setScene(scene);
            LoginApplication.getStage().show();
        }

    /**
     * Navigates to the message creation screen and passes the manager object to the new screen.
     */
    public void showCreateMessageScreen(){
        FXMLLoader loader = new FXMLLoader(LoginApplication.class.getResource("createNewMessageScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), MAIN_SCREEN_WIDTH, MAIN_SCREEN_HEIGHT);
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }

        CreateNewMessageScreenController createMessageController = loader.getController();
        createMessageController.setManager(projectManager);

        LoginApplication.getStage().setScene(scene);
        LoginApplication.getStage().show();
    }

    /**
     * Navigates to the task assignment screen and passes the manager object to the new screen.
     */
        public void showAssignTaskScreen(){
            FXMLLoader loader = new FXMLLoader(LoginApplication.class.getResource("assignATaskScreen.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(loader.load(), MAIN_SCREEN_WIDTH, MAIN_SCREEN_HEIGHT);
            } catch (IOException e) {
                LogUtils.error(e.getMessage());
            }

            AssignATaskScreenController assignATaskScreenController = loader.getController();
            assignATaskScreenController.setManager(projectManager);

            LoginApplication.getStage().setScene(scene);
            LoginApplication.getStage().show();
        }

    /**
     * Navigates to the screen that displays all tasks within the manager’s company.
     */

    public void showAllTasksScreen(){
        FXMLLoader loader = new FXMLLoader(LoginApplication.class.getResource("allTasksScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), MAIN_SCREEN_WIDTH, MAIN_SCREEN_HEIGHT);
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }

        AllTasksScreenController allTasksScreenController = loader.getController();
        allTasksScreenController.setProjectManager(projectManager);

        LoginApplication.getStage().setScene(scene);
        LoginApplication.getStage().show();
    }

}
