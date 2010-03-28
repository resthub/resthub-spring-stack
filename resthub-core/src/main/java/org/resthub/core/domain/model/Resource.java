package org.resthub.core.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Resource model.
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlRootElement
public class Resource implements Serializable {

    private static final long serialVersionUID = -4312792070014313489L;
    private Long id;
    private Date creationDate;
    private Date modificationDate;

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
    @TableGenerator(name="resourceGenerator",table="RESOURCE_SEQUENCES",allocationSize=1) 
    @GeneratedValue(strategy=GenerationType.TABLE, generator="resourceGenerator")
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
     * Get creation date.
     * @return creation date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false)
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Set the creation date.
     * @param creationDate creation date
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Get modification date.
     * @return modification date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modification_date", nullable = false)
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * Set modification date.
     * @param modificationDate modification date
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
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
