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

import org.jcrom.annotations.JcrProperty;

/**
 * A content type item defined by a name and a type
 * 
 * @author pastis
 */
public class ContentTypeField extends Resource {

	/**
	 * uid used for serialization
	 */
	private static final long serialVersionUID = 4118378816881138121L;

	/**
	 * Store the type of the current content type field
	 */
	@JcrProperty
	private String fieldType;
	
	/**
	 * Create a new content
	 */
	public ContentTypeField() {
		super();
	}

	/**
	 * Create a new content
	 * 
	 * @param name
	 *            name of the content
	 */
	public ContentTypeField(String name) {
		super(name);
	}	

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * @return A String image of the current Content type item object.
	 */
	@Override
	public String toString() {
		return "ContentTypeItem: [path: " + this.path + ", name: " + this.name
				+ ", uuid: " + this.uid + ", creationDate: "
				+ this.creationDate + "title: " + this.getTitle() + "] "
				+ ", type: " + this.fieldType
				+ super.toString();
	}

}
