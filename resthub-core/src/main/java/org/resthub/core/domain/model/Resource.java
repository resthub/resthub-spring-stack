package org.resthub.core.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@XmlRootElement
public class Resource implements Serializable {

    private Long id;
    private String name;
    private String label;

    public Resource() {
        
    }

    public Resource(String name) {
        this.name = name;
    }

    public Resource(String name, String label) {
        this.name = name;
        this.label = label;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public Resource setName(String name) {
        this.name = name;
        return this;
    }

    @Column
    public String getLabel() {
        return null != label ? label : name;
    }

    public Resource setLabel(String label) {
        this.label = label;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Resource other = (Resource) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

}
