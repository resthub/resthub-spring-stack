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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.resthub.core.domain.model.identity.User;
import org.jcrom.annotations.JcrProperty;
import org.jcrom.annotations.JcrReference;

/**
 * A content defined by a rich text body.
 * 
 * In the future, we may manager extensible contents (not a single 
 * body filed by a map of dynamics fields) and support more html tags thanks to 
 * http://code.google.com/p/htmlwrapper/ or some new Flex 4 features.
 * 
 * @author Bouiaw
 */
public class Content extends Resource {

    /**
     * uid used for serialization
     */
	private static final long serialVersionUID = 4118378816881138121L;

    /**
     * User who created this content
     */
	@JcrReference(byPath=true)
	private User author;
	
	/**
     * Content type
     */
	@JcrReference(byPath=true)
	private ContentType contentType;

    /**
     * date of the last update of this content
     */
	@JcrProperty private Calendar lastModified;
	
	/**
     * Text fields of this content
     */
	@JcrProperty
	private Map<String, String> textFields;
	
	/**
     * Reference to some content fields like file resources for example
     */
	@JcrReference
	private Map<String, Object> fieldReferences;
	
	public Map<String, String> getTextFields() {
		return textFields;
	}

	public void setTextFields(Map<String, String> textFields) {
		this.textFields = textFields;
	}
	
	/**
	 * Add a new text field to this content
	 * @return previous value associated to the name
	 */
	public String addTextField(String name, String value) {
		return this.textFields.put(name, value);
	}
	
	/**
	 * Remove the text field with this name 
	 * @return previous value associated to the name
	 */
	public String removeTextField(String name) {
		return this.textFields.remove(name);
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	/**
     * Create a new content 
     */
    public Content() {
    	super();
    	this.textFields = new HashMap<String, String>();
    	this.fieldReferences = new HashMap<String, Object>();
    }    
    
    /**
     * Create a new content
     * @param name name of the content
     */
    public Content(String name) {
    	super(name);
    	this.textFields = new HashMap<String, String>();
    	this.fieldReferences = new HashMap<String, Object>();
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * @return A String image of the current Content object.
     */
    @Override
    public String toString() {
        return "Content: [author: " + this.getAuthor() + ", lastModified: "
                + this.getLastModified() + ", title: " + this.getTitle() + "] " + super.toString();
    }

	public Map<String, Object> getFieldReferences() {
		return this.fieldReferences;
	}

	public void setFieldReferences(Map<String, Object> fieldReferences) {
		this.fieldReferences = fieldReferences;
	}

}
