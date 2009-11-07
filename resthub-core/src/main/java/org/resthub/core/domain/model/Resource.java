/**
 *  This file is part of Resthub.
 *
 *  Resthub is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *   Resthub is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Resthub.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.resthub.core.domain.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jcrom.annotations.JcrChildNode;
import org.jcrom.annotations.JcrCreated;
import org.jcrom.annotations.JcrName;
import org.jcrom.annotations.JcrParentNode;
import org.jcrom.annotations.JcrPath;
import org.jcrom.annotations.JcrProperty;
import org.jcrom.annotations.JcrUUID;

/**
 * Resource is the base of all classes that will be persisted in the JCR<
 * @author Bouiaw
 * 
 */
public class Resource implements IResource {

    /**
     * uid used for serialization
     */
	private static final long serialVersionUID = 3111079600921421436L;

    /**
     * Node path in the JCR, without the name part.
     */
    @JcrPath protected String path;
    
    /**
     * Node name in the JCR. Unlike title, the name identify the resource,
     * but is not aimed to be display on the website.
     */
    @JcrName protected String name;
    
    /**
     * uuid used to identify the Node on the repository 
     */
	@JcrUUID protected String uid;

    /**
     * Date when the resource has ben created.
     */
    @JcrCreated protected Calendar creationDate;
    
	/**
	 * The title is aimed to be display to the users
	 */
	@JcrProperty
	private String title;
    
    
	/**
	 * Resthub maximize model flexibility by allowing children on any king of Resource
	 */
    @JcrChildNode
    protected List<Resource> children;
	
	/**
	 * Parent resource where belong this one
	 */
	@JcrParentNode
	protected Resource parent;
	
    /**
     * Create a new resource 
     */
    public Resource() {
    	this.creationDate = Calendar.getInstance();
    	this.children = new ArrayList<Resource>();
    }    
    
    /**
     * Create a new resource
     * @param name name of the resource
     */
    public Resource(String name) {
        this.name = name;
        this.creationDate = Calendar.getInstance();
        this.children = new ArrayList<Resource>();
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	 public String getUid() {
	        return uid;
	    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    
    public List<Resource> getChildren() {
		return children;
	}

	public void setChildren(List<Resource> children) {
		this.children = children;
	}
	
	public void addChild(Resource child) {
		child.setParent(this);
		this.children.add(child);
	}

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
     * @return The unique hascode of an Resource is the hascode of its
     * path.
     */
    @Override
    public int hashCode() {
        return this.getUid().hashCode();
    }

    /**
     * @return a String image of the current Resource object. The generated String format is : 'Resource: [path:
     * [path],creationDate: [creationDate]]'.
     */
    @Override
    public String toString() {
        return "Resource: [path: " + this.path + ", name: " + this.name + ", uuid: "
        		+ this.uid + ", creationDate: " + this.creationDate + "]";
    }

	

}
