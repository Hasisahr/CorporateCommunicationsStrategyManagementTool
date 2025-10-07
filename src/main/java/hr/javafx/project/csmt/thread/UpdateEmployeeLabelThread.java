package hr.javafx.project.csmt.thread;

import hr.javafx.project.csmt.model.TeamMember;
import javafx.scene.control.Label;

import java.util.List;

/**
 * Runnable implementation that updates a warning label in the UI
 * with names of employees who already have two tasks assigned.
 * Helps visually alert the user when task assignment capacity is near its threshold
 * before assigning a third task.
 * This class is used in the assignment screen to ensure {@link TeamMember} task capacity.
 *
 */
public class UpdateEmployeeLabelThread implements Runnable {
    List<TeamMember> teamMembers;
    Label warningLabel;

    /**
     * Constructs a new instance with the list of team members to monitor
     * and the label to update with warnings.
     *
     * @param teamMembers the list of team members being evaluated
     * @param warningLabel the label in the UI to update
     */
    public UpdateEmployeeLabelThread(List<TeamMember> teamMembers, Label warningLabel) {
        this.teamMembers = teamMembers;
        this.warningLabel = warningLabel;
    }

    /**
     * Implementation of the method run from the {@link Runnable} interface
     * that filters team members who currently have exactly two tasks assigned to them
     * and updates the warning label with their names.
     */
    @Override
    public void run() {

        teamMembers = teamMembers.stream()
                .filter(t -> t.getTasks().size() == 2)
                .toList();
        if (!teamMembers.isEmpty()) {
            StringBuilder sb = new StringBuilder("Warning, these employees already have 2 task assigned:");
            for (TeamMember t : teamMembers) {
                sb.append("\n");
                sb.append(t.getName());
            }
            warningLabel.setText(sb.toString());
        }
    }
}
