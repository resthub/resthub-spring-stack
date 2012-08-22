package org.resthub.jpa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * This is a test entity to validate that we are able to manipulate resource entities with repositories
 */
@Entity
public class StandaloneEntity {

    private Long id;
    private String name;

    public StandaloneEntity() {
        super();
    }

    public StandaloneEntity(String name) {
        super();
        this.name = name;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StandaloneEntity[" + getId() + "," + getName() + "]";
    }
}
