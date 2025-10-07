package hr.javafx.project.csmt.repository;

import java.util.List;

/**
 * Abstract base class representing a generic repository class for handling data.
 * Defines core methods for retrieving and saving entities of type T.
 * This class is sealed and permits a defined set of repository implementations.
 *
 * @param <T> the type of object this repository manages
 *
 */

public abstract sealed class AbstractRepository<T> permits CompanyDatabaseRepository, CompanyFileRepository, EmployeeDatabaseRepository, LoginFileRepository, TaskDatabaseRepository {
    public abstract T findById(Long id);
    public abstract List<T> findAll();
    public abstract void save(T t);
}
