package hr.javafx.project.csmt.model;

import hr.javafx.project.csmt.enums.Role;

/**
 * Abstract class representing an employee entity.
 * Contains basic information such as name, role, and the employing company.
 * This class extends {@link Entity} to inherit the unique ID functionality.
 *
 */


public abstract class Employee extends Entity {
    String name;
    Role role;
    Company employer;

    protected Employee(Long id, String name, Role role, Company employer) {
        super(id);
        this.name = name;
        this.role = role;
        this.employer = employer;
    }

    public Company getEmployer() {
        return employer;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return  name;
    }
}
