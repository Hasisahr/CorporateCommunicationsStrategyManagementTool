package hr.javafx.project.csmt.controller.manager;

import hr.javafx.project.csmt.enums.TaskCompletion;
import hr.javafx.project.csmt.exception.TaskAndMessageCreationException;
import hr.javafx.project.csmt.model.Change;
import hr.javafx.project.csmt.model.ProjectManager;
import hr.javafx.project.csmt.model.Task;
import hr.javafx.project.csmt.repository.TaskDatabaseRepository;
import hr.javafx.project.csmt.utils.AlertUtils;
import hr.javafx.project.csmt.utils.ShowScreenUtils;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.time.LocalDate;

/**
 * Controller that allows project managers to create a new {@link Task} for the company.
 * Handles form validation, task instantiation, database inserting, and system change logging.
 * Prompts user confirmation before finalizing the task creation.
 *
 */
public class CreateTaskController {

    ProjectManager manager;

    /**
     * Sets the currently logged-in {@link ProjectManager}.
     *
     * @param manager the current project manager logged in
     */
    public void setManager(ProjectManager manager) {
        this.manager = manager;
    }

    @FXML
    TextField taskNameTextField;

    @FXML
    TextArea taskDescriptionTextArea;

    @FXML
    DatePicker taskDatePicker;

    /**
     * Navigates back to the main screen for the current project manager.
     */
    public void back(){
        ShowScreenUtils showScreenUtils = new ShowScreenUtils();
        showScreenUtils.showProjectManagerScreen(manager);
    }

    /**
     * Validates task input fields and creates a new {@link Task},
     * saves it to the database, and records a change entry.
     * If any fields are empty, shows an error alert to the user.
     *
     * @throws TaskAndMessageCreationException if task creation is invalid
     * @throws InterruptedException if change logging is interrupted
     */
    public void createTask() throws TaskAndMessageCreationException, InterruptedException {
        StringBuilder errors = new StringBuilder();
        String taskName = taskNameTextField.getText();
        String taskDescription = taskDescriptionTextArea.getText();
        LocalDate taskDate = taskDatePicker.getValue();
        if (taskName.isEmpty() || taskDescription.isEmpty() || taskDate == null) {
            errors.append("Please fill out all fields\n");
        }
        if (errors.isEmpty() && taskDate != null) {
            Task task = new Task(0L, taskName, taskDescription, taskDate, TaskCompletion.WAITING_ASSIGNMENT, manager.getName(), manager.getEmployer().getId());
            StringBuilder sb = new StringBuilder("Task:\n");
            sb.append(taskName)
                    .append("\nDescription:\n")
                    .append(taskDescription);
            if(Boolean.TRUE.equals(AlertUtils.showConfirmationAlert("Are you sure you want to save", String.valueOf(sb), "task"))) {
                TaskDatabaseRepository taskDatabaseRepository = new TaskDatabaseRepository();
                taskDatabaseRepository.save(task);
                String startingValue = "";
                String endingValue = "Task:" + task.getId() + " " + task.getName() + " " + task.getDescription();
                Change change = new Change("Task created", startingValue, endingValue, manager.getRole(), manager.getName());
                change.save();
            }
            manager.getEmployer().refreshTasks();
        }
        else{
           AlertUtils.showErrorAlert("task", errors.toString());
        }
    }
}
