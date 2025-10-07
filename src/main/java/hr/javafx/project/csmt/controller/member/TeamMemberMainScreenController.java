package hr.javafx.project.csmt.controller.member;

import hr.javafx.project.csmt.LoginApplication;
import hr.javafx.project.csmt.model.TeamMember;
import hr.javafx.project.csmt.utils.LogUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

import static hr.javafx.project.csmt.LoginApplication.MAIN_SCREEN_HEIGHT;
import static hr.javafx.project.csmt.LoginApplication.MAIN_SCREEN_WIDTH;

/**
 * Controller for the main screen accessible to a {@link TeamMember}.
 * Provides navigation options to view the member's tasks and company messages.
 * Maintains user context across views through controller injection.
 * This screen serves as the starting point for team members after logging in.
 *
 */
public class TeamMemberMainScreenController {
    TeamMember teamMember;

    /**
     * Sets the currently logged-in {@link TeamMember}.
     *
     * @param teamMember the current project manager logged in
     */
    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public void showMyTasksScreen(){
        FXMLLoader loader = new FXMLLoader(LoginApplication.class.getResource("myTasksScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), MAIN_SCREEN_WIDTH, MAIN_SCREEN_HEIGHT);
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }

        MyTasksScreenController myTasksScreenController = loader.getController();
        myTasksScreenController.setTeamMember(teamMember);

        LoginApplication.getStage().setScene(scene);
        LoginApplication.getStage().show();
    }


    /**
     * Opens the dashboard screen showing messages posted by project managers.
     */
    public void showDashboardScreen(){
        FXMLLoader loader = new FXMLLoader(LoginApplication.class.getResource("dashboard.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), MAIN_SCREEN_WIDTH, MAIN_SCREEN_HEIGHT);
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }

        DashboardController dashboardController = loader.getController();
        dashboardController.setTeamMember(teamMember);

        LoginApplication.getStage().setScene(scene);
        LoginApplication.getStage().show();
    }
}
