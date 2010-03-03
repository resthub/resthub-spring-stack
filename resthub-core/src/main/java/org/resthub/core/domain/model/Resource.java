package org.resthub.core.domain.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Resource model.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@XmlRootElement
public class Resource implements Serializable {

    private static final long serialVersionUID = -4312792070014313489L;
    private Long id;

    /**
     * Default constructor.
     */
    public Resource() {
        super();
    }

    /**
     * Get the resource id.
     * @return resource id
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
     * Set the resource id.
     * @param id resource id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Resource other = (Resource) obj;
        if ((this.id == null) ? (other.getId() != null) : !this.id.equals(other.getId())) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
