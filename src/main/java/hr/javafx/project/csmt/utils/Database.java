package hr.javafx.project.csmt.utils;
import hr.javafx.project.csmt.enums.TaskCompletion;
import hr.javafx.project.csmt.exception.DatabaseEmptyException;
import hr.javafx.project.csmt.exception.DatabaseException;
import hr.javafx.project.csmt.exception.MessageException;
import hr.javafx.project.csmt.model.*;
import hr.javafx.project.csmt.repository.CompanyDatabaseRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

//161 linija bez javadoca

/**
 * Utility class for database operations and connection management.
 * Provides methods for establishing database connections , fetching tasks and messages
 * associated with specific companies or entities, and retrieving next available entity IDs.
 * This class is intended to be used as a shared data access layer across the application.
 *
 */
public class Database {
    /**
     * Establishes a database connection using credentials loaded from the properties file.
     *
     * @return a Connection instance or {@code null} if the connection fails
     */
    public Connection getConnection() {
        Properties properties = new Properties();
        try(BufferedReader br = new BufferedReader(new FileReader("dat/databaseProperties.txt"))) {
            properties.load(br);
        }catch (IOException e) {
            LogUtils.error(e.getMessage());
        }

        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("pass");

        try  {
            return DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e){
            LogUtils.error(e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves the next available employee ID by scanning the EMPLOYEE table.
     *
     * @return the next sequential employee ID
     * @throws DatabaseException on SQL failure
     */
    public Long getEmployeeIdFromDatabase(){
        long id = 0L;
        try(Connection connection = getConnection()){
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT ID FROM EMPLOYEE");
                while(resultSet.next()){
                    id = resultSet.getLong("ID");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return id + 1;
    }

    /**
     * Retrieves the next available message ID by scanning the MESSAGE table.
     *
     * @return the next sequential message ID
     * @throws DatabaseException on SQL failure
     */
    public Long getMessageIdFromDatabase(){
        long id = 0L;
        try(Connection connection = getConnection()){
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT ID FROM MESSAGE");
                while(resultSet.next()){
                    id = resultSet.getLong("ID");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return id + 1;
    }

    /**
     * Retrieves the next available company ID by scanning the COMPANY table.
     *
     * @return the next sequential company ID
     * @throws DatabaseException on SQL failure
     */
    public Long getCompanyIdFromDatabase(){
        long id = 0L;
        try(Connection connection = getConnection()){
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT ID FROM COMPANY");
                while(resultSet.next()){
                    id = resultSet.getLong("ID");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return id + 1;
    }

    /**
     * Retrieves a filtered list of uncompleted tasks from the TASK table
     * for a given field (e.g., COMPANY_ID or EMPLOYEE_ID) and identifier.
     *
     * @param blankId the column to match (e.g., "COMPANY_ID")
     * @param id the corresponding value to search for
     * @return a list of matching {@link Task} objects
     * @throws DatabaseEmptyException if no tasks are found
     */
    public List<Task> getTasksListFromDatabase(String blankId, Long id) throws DatabaseEmptyException {
        Database database = new Database();
        List<Task> tasks = new ArrayList<>();
        try(Connection connection = database.getConnection()) {
            String sql = "SELECT ID, NAME, DESCRIPTION, DATE_DUE, COMPLETION, CREATED_BY FROM TASK WHERE " + blankId +
                    " = ?";
            try(PreparedStatement statement = connection.prepareStatement(sql)){

                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) {
                    Long taskId = resultSet.getLong("ID");
                    String taskName = resultSet.getString("NAME");
                    String taskDescription = resultSet.getString("DESCRIPTION");
                    LocalDate taskDateDue = LocalDate.parse(resultSet.getDate("DATE_DUE").toString());
                    TaskCompletion taskCompletion = TaskCompletion.valueOf(resultSet.getString("COMPLETION"));
                    String createdBy = resultSet.getString("CREATED_BY");
                    Task task = new Task(taskId, taskName, taskDescription, taskDateDue, taskCompletion, createdBy, id);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        tasks = tasks.stream()
                .filter(t->!t.getCompletion().equals(TaskCompletion.COMPLETED))
                .toList();
        if(tasks.isEmpty()){
            throw new DatabaseEmptyException("No tasks found");
        }
        return tasks;
    }

    /**
     * Retrieves all messages linked to a specific company by ID.
     * Reconstructs each {@link Message} from the MESSAGE table and
     * associates it with its {@link ProjectManager} and {@link Task} using a {@link Pair}.
     *
     * @param companyId the ID of the company to retrieve messages for
     * @return a set of {@link Message} objects associated with the company
     * @throws MessageException if no messages are found or a SQL error occurs
     */
    public Set<Message> getMessagesFromDatabase(Long companyId) throws MessageException {
        Set<Message> messages = new HashSet<>();
        try(Connection connection = getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT ID, NAME, DESCRIPTION, TASK_ID, PROJECT_MANAGER_ID, COMPANY_ID FROM MESSAGE WHERE COMPANY_ID = ?")) {
                statement.setLong(1, companyId);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) {
                    Long id = resultSet.getLong("ID");
                    String name = resultSet.getString("NAME");
                    String description = resultSet.getString("DESCRIPTION");
                    CompanyDatabaseRepository companyDatabaseRepository = new CompanyDatabaseRepository();
                    long taskId = resultSet.getLong("TASK_ID");
                    Company company = companyDatabaseRepository.findById(companyId);
                    Task task;
                    if(taskId != -1) {
                        task = company.getTasksList().stream()
                                .filter(t -> t.getId().equals(taskId))
                                .findFirst().orElse(null);
                    }
                    else{
                        task = null;
                    }
                    Long projectManagerId = resultSet.getLong("PROJECT_MANAGER_ID");

                    ProjectManager projectManager = (ProjectManager) company.getEmployeesList().stream()
                                .filter(e -> e.getId().equals(projectManagerId))
                                .findFirst()
                                .orElse(null);

                    Pair<ProjectManager, Task> pair = new Pair<>(projectManager, task);

                    Message message = new Message(id, name, description, pair);
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        if(messages.isEmpty()){
            throw new MessageException("No messages found");
        }
        return messages;
    }
}
