package hr.javafx.project.csmt.controller.manager;

import hr.javafx.project.csmt.enums.Role;
import hr.javafx.project.csmt.model.ProjectManager;
import hr.javafx.project.csmt.model.Task;
import hr.javafx.project.csmt.model.TeamMember;
import hr.javafx.project.csmt.utils.ShowScreenUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Controller responsible for displaying all tasks related to the project manager's company.
 * Populates a TableView with task data including name, assigned team member,
 * due date, and completion status. Assignments are resolved by linking each task
 * to its corresponding {@link TeamMember} based on task ownership.
 * Connected to a JavaFX view and requires the {@link ProjectManager} to be registered
 * before initialization. Also supports returning to the main screen.
 *
 */
public class AllTasksScreenController {

    ProjectManager projectManager;

    @FXML
    TableView<Task> allTasksTable;
    @FXML
    TableColumn<Task, String> taskNameColumn;
    @FXML
    TableColumn<Task, String> assignedToColumn;
    @FXML
    TableColumn<Task, String> dateDueColumn;
    @FXML
    TableColumn<Task, String> taskCompletionColumn;

    /**
     * Sets the currently logged-in {@link ProjectManager} and initializes the table with company tasks.
     *
     * @param projectManager the current project manager logged in
     */
    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
        initializeTableView();
    }

    /**
     * Initializes the table columns and defines how values are extracted and rendered.
     * Resolves task assignments by checking which {@link TeamMember} owns each task.
     */
    public void initialize(){
        taskNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        assignedToColumn.setCellValueFactory(cellData -> {
            Task task = cellData.getValue();
            TeamMember assignedTo = projectManager.getEmployer().getEmployeesList().stream()
                    .filter(e -> e.getRole().equals(Role.TEAM_MEMBER))
                    .map(e-> (TeamMember)e)
                    .filter(t -> t.getTasks().stream()
                            .anyMatch(taskItem -> taskItem.getId().equals(task.getId())))
                    .findFirst()
                    .orElse(null);
            String employeeName = "";
            if(assignedTo != null){
                employeeName = assignedTo.getName();
            }
            return new SimpleStringProperty(employeeName);
        });
        dateDueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDue().toString()));
        taskCompletionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompletion().toString()));
        allTasksTable.getItems().addAll();
    }

    /**
     * Loads the task data into the TableView for display.
     */
    private void initializeTableView() {
        List<Task> tasks = projectManager.getEmployer().getTasksList();
        ObservableList<Task> observableList = FXCollections.observableList(tasks);
        allTasksTable.setItems(observableList);
    }


    /**
     * Navigates back to the project manager's main screen calling {@link ShowScreenUtils}.
     */
    public void back(){
        ShowScreenUtils showScreenUtils = new ShowScreenUtils();
        showScreenUtils.showProjectManagerScreen(projectManager);
    }
}
