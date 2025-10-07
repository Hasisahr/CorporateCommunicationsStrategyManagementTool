package hr.javafx.project.csmt.repository;

import hr.javafx.project.csmt.enums.TaskCompletion;
import hr.javafx.project.csmt.exception.DatabaseException;
import hr.javafx.project.csmt.model.Task;
import hr.javafx.project.csmt.utils.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation for accessing and managing {@link Task} entities via database operations.
 * Supports retrieving tasks by ID, fetching all tasks from the database, and saving new tasks
 * by executing corresponding SQL statements.
 * This class is non-sealed and extends {@link AbstractRepository}.
 *
 */

public non-sealed class TaskDatabaseRepository extends AbstractRepository<Task> {


    /**
     * Retrieves a task by its unique identifier from the database.
     * Loads all tasks and filters them by the id.
     *
     * @param id the ID of the task to retrieve
     * @return the matching {@link Task}, or {@code null} if not found
     */
    @Override
    public Task findById(Long id) {
        return findAll().stream().filter(task -> task.getId().equals(id)).findFirst().orElse(null);
    }


    /**
     * Retrieves all tasks from the database and maps each record
     * to a {@link Task} object using an SQL SELECT statement.
     *
     * @return a list of all tasks stored in the database
     */
    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        Database database = new Database();
        try(Connection connection = database.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT ID, NAME, DESCRIPTION, DATE_DUE, COMPLETION, CREATED_BY, COMPANY_ID FROM TASK");
                while (resultSet.next()) {
                    Long id = resultSet.getLong("ID");
                    String name = resultSet.getString("NAME");
                    String description = resultSet.getString("DESCRIPTION");
                    LocalDate dateDue = LocalDate.parse(resultSet.getString("DATE_DUE"));
                    TaskCompletion status = TaskCompletion.valueOf(resultSet.getString("COMPLETION"));
                    String createdBy = resultSet.getString("CREATED_BY");
                    Long companyId = resultSet.getLong("COMPANY_ID");
                    Task task = new Task(id, name, description, dateDue, status,createdBy, companyId);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return tasks;
    }

    /**
     * Saves a new task to the database using an SQL INSERT statement.
     *
     * @param task the task entity to persist
     */
    @Override
    public void save(Task task) {
        Database database = new Database();
        try(Connection connection = database.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TASK(NAME, DESCRIPTION, DATE_DUE, COMPLETION, CREATED_BY, COMPANY_ID) VALUES (?,?,?,?,?,?)")){
                preparedStatement.setString(1, task.getName());
                preparedStatement.setString(2, task.getDescription());
                preparedStatement.setDate(3, Date.valueOf(task.getDue()));
                preparedStatement.setString(4, task.getCompletion().toString());
                preparedStatement.setString(5, task.getCreatedBy());
                preparedStatement.setLong(6, task.getCompanyId());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while adding task to the database", e);
        }
    }
}
