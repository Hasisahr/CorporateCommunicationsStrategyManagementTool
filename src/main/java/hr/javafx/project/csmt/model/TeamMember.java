package hr.javafx.project.csmt.model;

import hr.javafx.project.csmt.enums.Role;
import hr.javafx.project.csmt.exception.DatabaseEmptyException;
import hr.javafx.project.csmt.exception.DatabaseException;
import hr.javafx.project.csmt.utils.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Team Member which is a type of Employee.
 * This class is instantiated using the {@link ProjectManager.Builder} class.
 *
 */
public class TeamMember extends Employee{
    private List<Task> tasks;

    public TeamMember(Builder builder){
        super(builder.id, builder.name, builder.role, builder.employer);
        this.tasks = builder.tasks;
    }

    /**
     * Builder pattern class for constructing {@link TeamMember} instances.
     */

    public static class Builder{
        Long id;
        String name;
        Role role;
        Company employer;
        List<Task> tasks;

        public Builder withId(Long id){
            this.id = id;
            return this;
        }
        public Builder withName(String name){
            this.name = name;
            return this;
        }
        public Builder withRole(Role role){
            this.role = role;
            return this;
        }
        public Builder withEmployer(Company employer){
            this.employer = employer;
            return this;
        }
        public Builder withTasks(){
            Database database = new Database();
            try {
                this.tasks = database.getTasksListFromDatabase("EMPLOYEE_ID", id);
            }catch(DatabaseEmptyException e){
                this.tasks = new ArrayList<>();
            }
            return this;
        }

        /**
         * Builds and returns a new {@link TeamMember} instance using the provided data.
         *
         * @return a new TeamMember object
         */
        public TeamMember build(){
            return new TeamMember(this);
        }
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Assigns a task to this team member by updating the task's status to "IN_PROGRESS".
     * This method executes an SQL update query on the TASK table,
     * setting the EMPLOYEE_ID and COMPLETION fields.
     * @param task the task to be assigned to the team member
     * @throws DatabaseException if a SQL error occurs during the update
     */

    public void addTaskToTeamMember(Task task){
        Database database = new Database();
        try(Connection connection = database.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TASK SET EMPLOYEE_ID = ?, COMPLETION = ? WHERE ID = ?")){
                preparedStatement.setLong(1, getId());
                preparedStatement.setString(2, "IN_PROGRESS");
                preparedStatement.setLong(3, task.getId());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DatabaseException(e);
        }

    }
}
