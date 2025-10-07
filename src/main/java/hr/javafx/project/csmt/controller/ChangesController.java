package hr.javafx.project.csmt.controller;

import hr.javafx.project.csmt.model.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * JavaFX controller responsible for displaying a list of changes made throughout the application
 * such as adding a new message, task, etc.
 * Uses a ListView of VBox elements to show change details such as
 * name, original and new values, date of modification, and user information.
 *
 */
public class ChangesController {
    @FXML
    ListView<VBox> changesListView;


    /**
     * Loads change entities and fills the changesListView using the findAll() method
     * from the {@link Change} class
     * with information displayed in VBox containers.
     *
     * @throws InterruptedException if file access is interrupted during data load
     */
    public void initialize() throws InterruptedException {
        Change change = new Change();
        List<Change> changes;
        try {
            changes = change.findAll();
        } catch (InterruptedException e) {
            throw new InterruptedException("Thread interrupted");
        }
        for(Change c : changes) {
            VBox vbox = new VBox();
            Label nameLabel = new Label(c.getName());
            Label startingValueLabel = new Label("Starting value:" + c.getStartingValue());
            Label endingValueLabel = new Label("Ending value:" + c.getEndingValue());
            Label dateOfChangeLabel = new Label("Date of change:" + c.getDateOfChange());
            Label changedByName = new Label("Changed by:" + c.getChangedByName());
            Label changedByRole = new Label("Changed by:" + c.getChangedByRole());
            vbox.getChildren().addAll(nameLabel, startingValueLabel, endingValueLabel, dateOfChangeLabel, changedByName, changedByRole);
            changesListView.getItems().add(vbox);
        }
    }
}
