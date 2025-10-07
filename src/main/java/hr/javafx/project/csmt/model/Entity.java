package hr.javafx.project.csmt.model;

import java.io.Serializable;

/**
 * Abstract base class representing an entity with a unique identifier.
 * All classes extending this class must define their own properties,
 * while inheriting the common ID field.
 * This class also implements Serializable, allowing instances to be serialized.
 *
 */

public abstract class Entity implements Serializable {
    private Long id;

    protected Entity(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
