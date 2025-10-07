package hr.javafx.project.csmt.model;

import hr.javafx.project.csmt.enums.Role;
import hr.javafx.project.csmt.exception.DatabaseEmptyException;
import hr.javafx.project.csmt.exception.DatabaseException;
import hr.javafx.project.csmt.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a company entity which extends {@link Entity} and holds
 * information about its name, employees, and assigned tasks.
 * Upon instantiation, the list of employees is fetched from the database,
 * and the tasks are refreshed automatically.
 * This class also provides functionality to refresh task and employee data
 * from the database manually.
 *
 */


public class Company extends Entity{
    private String name;
    private List<Task> tasksList;
    private List<Employee> employeesList;

    public Company(Long id, String name) {
        super(id);
        this.name = name;
        this.employeesList = getEmployeesListFromDatabase(id);
        refreshTasks();
    }

    public String getName() {
        return name;
    }


    public List<Task> getTasksList() {
        return tasksList;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Employee> getEmployeesList() {
        return employeesList;
    }


    public void refreshTasks(){
        Database database = new Database();
        try {
            this.tasksList = database.getTasksListFromDatabase("COMPANY_ID", getId());
        }catch (DatabaseEmptyException e){
            this.tasksList = new ArrayList<>();
        }
    }

    public List<Employee> getEmployeesListFromDatabase(Long id) {
        Database database = new Database();
        List<Employee> employees = new ArrayList<>();
        try(Connection connection = database.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, NAME, ROLE FROM EMPLOYEE WHERE COMPANY_ID = ?")) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    Long employeeId = resultSet.getLong("ID");
                    String employeeName = resultSet.getString("NAME");
                    Role role = Role.valueOf(resultSet.getString("ROLE"));
                    Company employeeCompany = this;
                    if(role.equals(Role.TEAM_MEMBER)){
                        TeamMember.Builder builder = new TeamMember.Builder();
                        TeamMember teamMember = builder.withId(employeeId)
                                .withName(employeeName)
                                .withRole(role)
                                .withEmployer(employeeCompany)
                                .withTasks()
                                .build();
                        employees.add(teamMember);
                    }
                    else{
                        ProjectManager.Builder builder = new ProjectManager.Builder();
                        ProjectManager projectManager = (ProjectManager) builder.withId(employeeId)
                                .withName(employeeName)
                                .withRole(role)
                                .withEmployer(employeeCompany)
                                .build();
                        employees.add(projectManager);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return employees;
    }
    public void refreshEmployees(){
        this.employeesList = getEmployeesListFromDatabase(getId());
    }
}
