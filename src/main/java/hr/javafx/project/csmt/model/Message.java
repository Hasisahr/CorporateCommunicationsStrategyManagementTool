package hr.javafx.project.csmt.model;

import java.time.LocalDate;

/**
 * Represents a message entity used for communication or logging between
 * Project Managers and Team Members.
 * It contains the message title, content, creation date, and a pairing of manager and task.
 * The {@code pair} is used for associating the Project Manager that is posting the message
 * and the {@link Task} it refers to.
 */

public class Message extends Entity {
    String title;
    String content;
    LocalDate dateCreated;
    transient Pair<ProjectManager, Task> pair;

    public Message(Long id, String title, String content, Pair<ProjectManager, Task> pair) {
        super(id);
        this.title = title;
        this.content = content;
        this.dateCreated = LocalDate.now();
        this.pair = pair;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }
    public Pair<ProjectManager, Task> getPair() {
        return pair;
    }
}
