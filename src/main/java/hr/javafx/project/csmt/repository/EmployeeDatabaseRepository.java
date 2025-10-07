package hr.javafx.project.csmt.repository;

import hr.javafx.project.csmt.enums.Role;
import hr.javafx.project.csmt.exception.DatabaseException;
import hr.javafx.project.csmt.model.Company;
import hr.javafx.project.csmt.model.Employee;
import hr.javafx.project.csmt.model.ProjectManager;
import hr.javafx.project.csmt.model.TeamMember;
import hr.javafx.project.csmt.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation for managing {@link Employee} entities through database access.
 * Supports finding employees by ID, retrieving all employees, and saving new entries
 * to the underlying database. Converts SQL records into either {@link TeamMember} or
 * {@link ProjectManager} instances based on role.
 *
 */

public non-sealed class EmployeeDatabaseRepository extends AbstractRepository<Employee> {

    /**
     * Retrieves an employee by ID using in-memory filtering after loading all employees.
     *
     * @param id the ID of the employee to retrieve
     * @return the matching {@link Employee} instance, or {@code null} if not found
     */
    @Override
    public Employee findById(Long id) {
        return findAll().stream().filter(employee -> employee.getId().equals(id)).findAny().orElse(null);
    }

    /**
     * Loads all employee records from the database using the SQL SELECT and maps them to either
     * {@link TeamMember} or {@link ProjectManager} objects based on the role of the employee written in the database.
     *
     * @return a list of all employees retrieved from the database
     */
    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        Database database = new Database();
        try(Connection connection = database.getConnection()) {
            ResultSet resultSet;
            try (Statement statement = connection.createStatement()) {
                resultSet = statement.executeQuery("SELECT ID, NAME, ROLE, COMPANY_ID FROM employee");
                while (resultSet.next()) {
                    Long id = resultSet.getLong("ID");
                    String name = resultSet.getString("NAME");
                    Role role = Role.valueOf(resultSet.getString("ROLE"));
                    Long companyId = resultSet.getLong("COMPANY_ID");
                    CompanyDatabaseRepository repository = new CompanyDatabaseRepository();
                    Company company = repository.findById(companyId);
                    if(resultSet.getString("ROLE").equals("PROJECT_MANAGER")) {
                        ProjectManager.Builder builder = new ProjectManager.Builder();
                        ProjectManager projectManager = (ProjectManager) builder.withId(id)
                                .withName(name)
                                .withRole(role)
                                .withEmployer(company)
                                .build();
                        employees.add(projectManager);
                    }
                    else {
                        TeamMember.Builder builder = new TeamMember.Builder();
                        TeamMember teamMember = builder.withId(id)
                                .withName(name)
                                .withRole(role)
                                .withEmployer(company)
                                .withTasks()
                                .build();
                        employees.add(teamMember);
                    }
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return employees;
    }

    /**
     * Inserts a new employee record into the database using SQL INSERT.
     *
     * @param employee the employee entity to persist
     */
    @Override
    public void save(Employee employee) {
        Database database = new Database();
        try(Connection connection = database.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO EMPLOYEE(NAME, ROLE, COMPANY_ID) VALUES (?,?,?)")){
                preparedStatement.setString(1, employee.getName());
                preparedStatement.setString(2, employee.getRole().toString());
                preparedStatement.setLong(3, employee.getEmployer().getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
