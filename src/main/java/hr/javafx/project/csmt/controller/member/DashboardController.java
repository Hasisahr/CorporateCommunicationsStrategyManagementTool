package hr.javafx.project.csmt.controller.member;

import hr.javafx.project.csmt.exception.MessageException;
import hr.javafx.project.csmt.model.Message;
import hr.javafx.project.csmt.model.TeamMember;
import hr.javafx.project.csmt.utils.Database;
import hr.javafx.project.csmt.utils.LogUtils;
import hr.javafx.project.csmt.utils.ShowScreenUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.Set;

/**
 * Controller for displaying all messages relevant to a {@link TeamMember}.
 * Retrieves messages linked to the team member's company from the database
 * and shows them in a ListView using VBox containers.
 * Each message entry includes title, content, date, optional task association,
 * and the project manager who posted it.
 *
 */
public class DashboardController {
    TeamMember teamMember;

    /**
     * Sets the currently logged-in {@link TeamMember}.
     *
     * @param teamMember the current project manager logged in
     */
    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
        initializeMessagesListView();
    }

    @FXML
    ListView<VBox> messagesListView;

    /**
     * Navigates back to the main screen for the current team member.
     */
    public void back(){
        ShowScreenUtils showScreenUtils = new ShowScreenUtils();
        showScreenUtils.showTeamMemberScreen(teamMember);
    }

    /**
     * Loads all messages from the database for a company
     * and shows them in a ListView.
     * Uses the {@link Database} to retrieve the messages from the database.
     */

    public void initializeMessagesListView(){
        Database database = new Database();
        try {
            Set<Message> messageSet = database.getMessagesFromDatabase(teamMember.getEmployer().getId());
            for (Message message : messageSet) {
                VBox vbox = new VBox();
                Label messageName = new Label(message.getTitle());
                Label messageContent = new Label(message.getContent());
                Label messageDate = new Label(message.getDateCreated().toString());
                vbox.getChildren().addAll(messageName, messageContent, messageDate);
                if (message.getPair().getSecond() != null) {
                    Label taskName = new Label(message.getPair().getSecond().getName());
                    vbox.getChildren().add(taskName);
                }
                Label messageCreator = new Label("Posted by:" + message.getPair().getFirst().getName());
                vbox.getChildren().add(messageCreator);
                messagesListView.getItems().add(vbox);
            }
        }
        catch (MessageException e){
            LogUtils.error(e.getMessage());
            Label noMessages = new Label("No messages to show...");
            VBox vbox = new VBox();
            vbox.getChildren().add(noMessages);
            messagesListView.getItems().add(vbox);
        }
    }
}
