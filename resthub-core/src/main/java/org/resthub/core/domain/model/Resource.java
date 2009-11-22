package org.resthub.core.domain.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Resource is the base of all classes that will be persisted in the JCR<
 * @author Bouiaw
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Resource implements Serializable {

    /**
     * uid used for serialization
     */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;

    /**
     * Resource path, used to retreive a Resource from an URL
     */
    protected String path;
    
    /**
     * Node name in the JCR. Unlike title, the name identify the resource,
     * but is not aimed to be display on the website.
     */
    private String name;

	/**
	 * Resthub maximize model flexibility by allowing children on any king of Resource
	 */
//    @ManyToMany(targetEntity = Resource.class)
//    protected List<Resource> children;
	
	/**
	 * Parent resource where belong this one
	 */
	protected Resource parent;
	
    /**
     * Create a new resource 
     */
    public Resource() {
    	//this.children = new ArrayList<Resource>();
    }    
    
    /**
     * Create a new resource
     * @param name name of the resource
     */
    public Resource(String name) {
        this.name = name;
        //this.children = new ArrayList<Resource>();
    }
    
    /**
     * Create a new resource
     * @param name name of the resource
     */
    public Resource(String name, String path) {
        super();
        this.name = name;
        this.path = path;
    }
    
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
        
    public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	 public Long getId() {
	        return this.id;
	    }

    public void setId(Long id) {
        this.id = id;
    }
    
//    public List<Resource> getChildren() {
//		return children;
//	}
//
//	public void setChildren(List<Resource> children) {
//		this.children = children;
//	}
//	
//	public void addChild(Resource child) {
//		child.setParent(this);
//		this.children.add(child);
//	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}
    
    /**
     * Allows to determine if two Resources are equals. The test is based on next criteria : The Object o has to be a
     * Resource instance and its path has to be equals to the current Resource path.
     * @param o : The Object to compare with the current Resource.
     * @return true if both resources are equals, false else
     */
    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (!(o instanceof Resource)) {
            result = false;
        } else {
            final Resource resource = (Resource) o;
            result = this.getPath().equals(resource.getPath());
        }

        return result;
    }
    
  

    /**
     * Allows to use Resources objects as keys in Maps.
     * @return The unique hashcode of an Resource is the hashcode of its
     * path.
     */
    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    /**
     * @return a String image of the current Resource object. The generated String format is : 'Resource: [path:
     * [path],creationDate: [creationDate]]'.
     */
    @Override
    public String toString() {
        return "Resource: [path: " + this.path + ", name: " + this.name + ", id: "
        		+ this.id + "]";
    }

	

}
