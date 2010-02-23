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

	private static final long serialVersionUID = -4312792070014313489L;
	
	private Long id;
    private String name;

    public Resource() {
        
    }

    public Resource(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
	 * @see org.resthub.core.domain.model.Resource#getId()
	 */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /* (non-Javadoc)
	 * @see org.resthub.core.domain.model.Resource#setId(java.lang.Long)
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /* (non-Javadoc)
	 * @see org.resthub.core.domain.model.Resource#getName()
	 */
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public Resource setName(String name) {
        this.name = name;
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
        if ((this.name == null) ? (other.getName() != null) : !this.name.equals(other.getName())) {
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
