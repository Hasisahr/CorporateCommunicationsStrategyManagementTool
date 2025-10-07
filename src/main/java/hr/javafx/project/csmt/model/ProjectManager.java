package hr.javafx.project.csmt.model;

import hr.javafx.project.csmt.enums.Role;
/**
 * Represents a Project Manager which is a type of Employee.
 * This class is instantiated using the {@link ProjectManager.Builder} class.
 *
 */

public class ProjectManager extends Employee {

    public ProjectManager(Builder builder){
        super(builder.id, builder.name, builder.role, builder.employer);
    }

    /**
     * Builder pattern class for constructing {@link ProjectManager} instances.
     */

    public static class Builder {
        Long id;
        String name;
        Role role;
        Company employer;

        public Builder withId(Long id){
            this.id = id;
            return this;
        }
        public Builder withRole(Role role){
           this.role = role;
           return this;
        }
        public Builder withName(String name){
            this.name = name;
            return this;
        }
        public Builder withEmployer(Company employer){
            this.employer = employer;
            return this;
        }
        /**
         * Builds and returns a new {@link ProjectManager} instance using the provided data.
         *
         * @return a new ProjectManager object
         */
        public Employee build(){
            return new ProjectManager(this);
        }
    }

}
