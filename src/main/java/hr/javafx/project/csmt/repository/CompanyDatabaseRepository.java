package hr.javafx.project.csmt.repository;

import hr.javafx.project.csmt.exception.DatabaseException;
import hr.javafx.project.csmt.model.Company;
import hr.javafx.project.csmt.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation for accessing {@link Company} entities from a database.
 * Provides methods to find companies by ID, fetch all companies,
 * and save new ones to the database.
 * This class is non-sealed and extends {@link AbstractRepository}.
 *
 */
public non-sealed class CompanyDatabaseRepository extends AbstractRepository<Company> {

    /**
     * Retrieves a company by its unique ID using {@code findAll()} and stream filtering
     * over all loaded companies.
     *
     * @param id the unique identifier of the company to retrieve
     * @return the {@code Company} with the matching ID, or {@code null} if not found
     */

    @Override
    public Company findById(Long id) {
        return findAll().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Loads all companies from the database using a SELECT query.
     *
     * @return a list of all companies stored in the database
     */

    @Override
    public List<Company> findAll() {
        List<Company> companies = new ArrayList<>();
        Database database = new Database();
        try(Connection con = database.getConnection()) {
            try (Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT ID, NAME FROM COMPANY");
                while (rs.next()) {
                    companies.add(new Company(rs.getLong("ID"), rs.getString("NAME")));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return companies;
    }


    /**
     * Saves a new company to the database using an INSERT query.
     *
     * @param company the company entity to save
     */
    @Override
    public void save(Company company) {
        Database database = new Database();
        try(Connection connection = database.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO COMPANY(NAME) VALUES (?)")){
                preparedStatement.setString(1, company.getName());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
