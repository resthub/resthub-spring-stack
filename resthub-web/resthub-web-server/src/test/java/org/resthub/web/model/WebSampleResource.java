package org.resthub.web.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WebSampleResource {

    private Long id;
    private String name;

    public WebSampleResource() {
        super();
    }

    public WebSampleResource(String name) {
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WebSampleResource other = (WebSampleResource) obj;
        if ((this.id == null) ? (other.getId() != null) : !this.id.equals(other.getId())) {
            return false;
        }
        if ((this.name == null) ? (other.getName() != null) : !this.name.equals(other.getName())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.id == null ? 0 : this.id.hashCode());
        return hash;
    }

    @Override
    public String toString() {
        return "WebSampleResource[" + getId() + "," + getName() + "]";
    }
}
