package org.resthub.web.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Sample {

    private Long id;
    private String name;

    public Sample() {
        super();
    }

    public Sample(String name) {
        super();
        this.name = name;
    }

    public Sample(Sample webSampleResource) {
        super();
        this.id = webSampleResource.getId();
        this.name = webSampleResource.getName();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sample)) return false;

        Sample sample = (Sample) o;

        return !(id != null ? !id.equals(sample.id) : sample.id != null)
                && !(name != null ? !name.equals(sample.name) : sample.name != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WebSampleResource[" + getId() + "," + getName() + "]";
    }
}
