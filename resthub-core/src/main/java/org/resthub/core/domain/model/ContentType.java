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

import java.util.List;

import org.jcrom.annotations.JcrChildNode;

/**
 * A content type defined by a comination of Text contents and file
 * resources contents.
 * 
 * @author pastis
 */
public class ContentType extends Resource {

    /**
     * uid used for serialization
     */
	private static final long serialVersionUID = 4118378816881138121L;

	
	/**
	 * Store a list of associated content type fields 
	 */
	@JcrChildNode
	private List<ContentTypeField> contentTypeFields;
	

	/**
     * Create a new content 
     */
    public ContentType() {
    	super();
    }    
    
    /**
     * Create a new content
     * @param name name of the content
     */
    public ContentType(String name) {
    	super(name);
    }
    
    /**
     * Get all content type fields
     */
	public List<ContentTypeField> getContentTypeFields() {
		return contentTypeFields;
	}

	/**
     * Set content type fields
     */
	public void setContentTypeFields(List<ContentTypeField> contentTypeFields) {
		this.contentTypeFields = contentTypeFields;
	}

	/**
     * @return A String image of the current Content object.
     */
    @Override
    public String toString() {
        return "ContentType: [title: " + this.getTitle() + "] " + super.toString();
    }

}
