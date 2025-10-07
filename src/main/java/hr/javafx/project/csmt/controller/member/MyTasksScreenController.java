package hr.javafx.project.csmt.controller.member;

import hr.javafx.project.csmt.enums.TaskCompletion;
import hr.javafx.project.csmt.enums.TaskPriority;
import hr.javafx.project.csmt.exception.DatabaseEmptyException;
import hr.javafx.project.csmt.exception.DatabaseException;
import hr.javafx.project.csmt.model.Task;
import hr.javafx.project.csmt.model.TeamMember;
import hr.javafx.project.csmt.utils.AlertUtils;
import hr.javafx.project.csmt.utils.Database;
import hr.javafx.project.csmt.utils.ShowScreenUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the "My Tasks" screen for a {@link TeamMember}.
 * Displays the list of tasks assigned to the logged-in team member,
 * allows viewing details of a selected task, and marking tasks as completed.
 * Task priority is colored based on {@link TaskPriority}.
 *
 */
public class MyTasksScreenController {
    TeamMember teamMember;

    /**
     * Sets the currently logged-in {@link TeamMember}.
     *
     * @param teamMember the current project manager logged in
     */
    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
        initializeMyTasks();
    }

    @FXML
    ListView<Task> tasksListView;

    @FXML
    VBox tasksInformationVBox;

    /**
     * Navigates back to the main screen for the current team member.
     */
    public void back(){
        ShowScreenUtils showScreenUtils = new ShowScreenUtils();
        showScreenUtils.showTeamMemberScreen(teamMember);
    }


    /**
     * Initializes the task list by filtering out completed tasks.
     * Fills the ListView with unassigned tasks.
     */
    public void initializeMyTasks(){
        tasksListView.getItems().clear();
        List<Task> tasks = teamMember.getTasks().stream()
                .filter(t -> !t.getCompletion().equals(TaskCompletion.COMPLETED))
                .toList();
        tasksListView.getItems().addAll(tasks);
    }


    /**
     * Displays task details, including priority indication and a completion button.
     * Reloads the task list after viewing or marking as complete.
     * Colors tasks based on its priority: green for low priority, yellow for medium
     * and red as a task with a high priority.
     */
    public void showTask(){
        tasksInformationVBox.getChildren().clear();

        Task task = tasksListView.getSelectionModel().getSelectedItem();
        if(task != null) {
            Label taskNameLabel = new Label(task.getName());
            Label taskDescriptionLabel = new Label(task.getDescription());
            Label taskDateDue = new Label("Date due: " + task.getDue().toString());
            Button finishedButton = new Button("Finished");

            TaskPriority taskPriority = task.getPriority(task.getDue());

            if (taskPriority != null) {
                if (taskPriority.equals(TaskPriority.HIGH_PRIORITY)) {
                    taskDateDue.setStyle("-fx-background-color: red;-fx-padding: 5px;");
                } else if (taskPriority.equals(TaskPriority.MEDIUM_PRIORITY)) {
                    taskDateDue.setStyle("-fx-background-color: yellow; -fx-padding: 5px;");
                } else {
                    taskDateDue.setStyle("-fx-background-color: green;-fx-padding: 5px;");
                }
            }

            taskDateDue.setWrapText(true);
            tasksInformationVBox.getChildren().addAll(taskNameLabel, taskDescriptionLabel, taskDateDue, finishedButton);
            tasksInformationVBox.setSpacing(10);

            Database db = new Database();
            finishedButton.setOnAction(event -> changeTaskCompletion(task));
            try {
                teamMember.setTasks(db.getTasksListFromDatabase("EMPLOYEE_ID", teamMember.getId()));
            } catch (DatabaseEmptyException e) {
                teamMember.setTasks(new ArrayList<>());
            }
        }


        initializeMyTasks();
    }


    /**
     * Updates the completion status of the selected task in the database.
     * Prompts the user with a confirmation before applying changes.
     * Uses SQL UPDATE statement for updating the task completion in the database.
     *
     * @param task the task to mark as completed
     */
    public void changeTaskCompletion(Task task){

        if(Boolean.TRUE.equals(AlertUtils.showConfirmationAlert("Are you sure you have finished this task?", "Task changed to finished", "Completion"))){
            Database database = new Database();
            try(Connection connection = database.getConnection()) {
                try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TASK SET COMPLETION = ? WHERE ID = ?")) {
                    preparedStatement.setString(1, TaskCompletion.COMPLETED.toString());
                    preparedStatement.setLong(2, task.getId());
                    preparedStatement.executeUpdate();

                }
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }


        }
    }
}
