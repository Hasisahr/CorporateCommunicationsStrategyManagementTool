package hr.javafx.project.csmt.controller.manager;

import hr.javafx.project.csmt.enums.Role;
import hr.javafx.project.csmt.enums.TaskCompletion;
import hr.javafx.project.csmt.model.*;
import hr.javafx.project.csmt.thread.UpdateEmployeeLabelThread;
import hr.javafx.project.csmt.utils.AlertUtils;
import hr.javafx.project.csmt.utils.LogUtils;
import hr.javafx.project.csmt.utils.ShowScreenUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Duration;

import java.util.List;

/**
 * JavaFX controller for assigning unassigned tasks to available team members.
 * Filters and displays tasks that are awaiting assignment, and employees
 * who are eligible to receive a task (based on max task threshold).
 * Saves changes and shows them on the screen.
 * It also logs changes and updates a dynamic warning label based on workload.
 *
 */
public class AssignATaskScreenController {
    ProjectManager manager;
    List<TeamMember> filteredEmployees;

    /**
     * Sets the currently logged-in {@link ProjectManager}.
     *
     * @param manager the current project manager logged in
     */
    public void setManager(ProjectManager manager) {
        this.manager = manager;
        initializeTaskList();
        initializeEmployeeList();
    }

    @FXML
    ListView<Task> taskListView;

    @FXML
    ListView<TeamMember> employeeListView;

    @FXML
    Label warningLabel;


    /**
     * Navigates back to the main project manager screen.
     */
    public void back(){
        ShowScreenUtils showScreenUtils = new ShowScreenUtils();
        showScreenUtils.showProjectManagerScreen(manager);
    }

    /**
     * Initializes the list of tasks that are still waiting to be assigned.
     * Filters the list based on task status.
     */
    public void initializeTaskList(){
        manager.getEmployer().refreshTasks();
        List<Task> tasks = manager.getEmployer().getTasksList();
        tasks = tasks.stream()
                .filter(t->t.getCompletion().equals(TaskCompletion.WAITING_ASSIGNMENT))
                .toList();
        ObservableList<Task> observableList;
        observableList = FXCollections.observableList(tasks);
        taskListView.setItems(observableList);
    }


    /**
     * Filters available team members based on their current task load,
     * associates a visual label warning using {@link UpdateEmployeeLabelThread},
     * and binds the data to the list.
     */
    public void initializeEmployeeList(){
        manager.getEmployer().refreshEmployees();
        List<TeamMember> teamMembers = manager.getEmployer().getEmployeesList().stream()
                .filter(e -> e.getRole().equals(Role.TEAM_MEMBER))
                .map(e -> (TeamMember) e)
                .toList();
        teamMembers = teamMembers.stream()
                .filter(u -> u.getEmployer().getId().equals(manager.getEmployer().getId()))
                .filter(u -> u.getTasks().size() < 3)
                .toList();
        this.filteredEmployees = teamMembers;
        UpdateEmployeeLabelThread updateEmployeeLabelThread = new UpdateEmployeeLabelThread(filteredEmployees, warningLabel);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> updateEmployeeLabelThread.run()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        ObservableList<TeamMember> observableList;
        observableList = FXCollections.observableList(teamMembers);
        employeeListView.setItems(observableList);
    }


    /**
     * Handles assignment of the selected task to the selected employee,
     * triggers confirmation, logs the change, updates both lists,
     * and records it in the system change log.
     *
     * @throws InterruptedException if saving the change is interrupted
     */
    public void assignATask() throws InterruptedException {
        Task task = taskListView.getSelectionModel().getSelectedItem();
        TeamMember teamMember = employeeListView.getSelectionModel().getSelectedItem();

        StringBuilder errors = new StringBuilder();

        if(task == null || teamMember == null){
            errors.append("Please select one task and one employee\n");
        }
        if(errors.isEmpty() && teamMember != null && task != null) {
            if(Boolean.TRUE.equals(AlertUtils.showConfirmationAlert("Are you sure you want to to assign task", task.getName(), "task assignment"))) {
                teamMember.addTaskToTeamMember(task);
                initializeTaskList();
                initializeEmployeeList();
                UpdateEmployeeLabelThread updateEmployeeLabelThread = new UpdateEmployeeLabelThread(filteredEmployees, warningLabel);
                updateEmployeeLabelThread.run();
            }
            String startingValue = "EMPLOYEE_ID = null " + "TaskCompletion = WAITING_ASSIGNMENT";
            String endingValue ="EMPLOYEE_ID = " + teamMember.getId() + "TaskCompletion = IN_PROGRESS";

            Change change = new Change("Task assigned", startingValue, endingValue, manager.getRole(), manager.getName());
            change.save();
            LogUtils.info(change.toString());
        }
        else{
            AlertUtils.showErrorAlert("Error assigning a task" ,errors.toString());
        }
    }
}
