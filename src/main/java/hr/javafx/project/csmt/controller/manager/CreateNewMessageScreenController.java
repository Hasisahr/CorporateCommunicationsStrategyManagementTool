package hr.javafx.project.csmt.controller.manager;

import hr.javafx.project.csmt.exception.DatabaseException;
import hr.javafx.project.csmt.exception.TaskAndMessageCreationException;
import hr.javafx.project.csmt.model.*;
import hr.javafx.project.csmt.utils.AlertUtils;
import hr.javafx.project.csmt.utils.Database;
import hr.javafx.project.csmt.utils.ShowScreenUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Controller for creating and saving new messages to the database.
 * Handles the logic for generating a new
 * {@link Message} object, optionally linking it to a selected {@link Task}, and
 * saves it in the database.
 * Logs the change and prompts.
 *
 */
public class CreateNewMessageScreenController {
    ProjectManager manager;

    /**
     * Sets the currently logged-in {@link ProjectManager}.
     *
     * @param manager the current project manager logged in
     */
    public void setManager(ProjectManager manager) {
        this.manager = manager;
        initializeListView();
    }

    @FXML
    TextField messageNameTextField;

    @FXML
    TextArea messageDescriptionTextField;

    @FXML
    ListView<Task> messageTaskListView;

    /**
     * Loads all company-related tasks into the message task ListView.
     */
    public void initializeListView() {
        messageTaskListView.getItems().addAll(manager.getEmployer().getTasksList());
    }
    /**
     * Navigates back to the main screen for the current project manager.
     */
    public void back(){
        ShowScreenUtils showScreenUtils = new ShowScreenUtils();
        showScreenUtils.showProjectManagerScreen(manager);
    }
    /**
     * Validates input fields, builds a {@link Message} object, confirms user intent,
     * saves it to the database, and logs the change to the system.
     *
     * @throws TaskAndMessageCreationException if message creation is invalid
     * @throws InterruptedException if change tracking is interrupted
     */
    public void addMessage() throws TaskAndMessageCreationException, InterruptedException {
        StringBuilder errors = new StringBuilder();
        if(messageNameTextField.getText().isEmpty() || messageDescriptionTextField.getText().isEmpty()){
            errors.append("Fill out name and description fields!\n");
        }

        if(errors.isEmpty()){
            String name = messageNameTextField.getText();
            String description = messageDescriptionTextField.getText();
            Task task = messageTaskListView.getSelectionModel().getSelectedItem();
            Pair<ProjectManager, Task> pair = new Pair<>(manager, task);

            Database database = new Database();
            Long id = database.getMessageIdFromDatabase();

            Message message = new Message(id, name, description, pair);

            StringBuilder sb = new StringBuilder("Message:");
            sb.append(name);
            sb.append('\n');
            sb.append(description);
            sb.append('\n');
            if(task != null) {
                sb.append(task.getName());
            }

            if(Boolean.TRUE.equals(AlertUtils.showConfirmationAlert("Are you sure you want to add this message", sb.toString(), "adding the message"))){
                uploadToDatabase(message);
                String startingValue = "";
                String endingValue = "Message:" + message.getId() + " " + message.getTitle() + " " + message.getContent();
                Change change = new Change("Message created", startingValue, endingValue, manager.getRole(), manager.getName());
                change.save();
            }
        }
        else{
            AlertUtils.showErrorAlert("Error adding a message", errors.toString());
        }

    }

    /**
     * Uploads the given message entity into the database using the SQL INSERT prepared statement.
     * @param message the message to be persisted
     */
    public void uploadToDatabase(Message message){
        Database database = new Database();
        try(Connection connection = database.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO MESSAGE(NAME, DESCRIPTION, TASK_ID, PROJECT_MANAGER_ID, COMPANY_ID) VALUES (?,?,?,?,?)")) {
                preparedStatement.setString(1, message.getTitle());
                preparedStatement.setString(2, message.getContent());
                if(message.getPair().getSecond() != null){
                    preparedStatement.setLong(3, message.getPair().getSecond().getId());
                }
                else{
                    preparedStatement.setLong(3, -1);
                }
                preparedStatement.setLong(4, message.getPair().getFirst().getId());
                preparedStatement.setLong(5, message.getPair().getFirst().getEmployer().getId());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
