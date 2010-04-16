package org.resthub.core.domain.model;

import javax.persistence.Entity;

/**
 * @author bmeurant <baptiste.meurant@gmail.com>
 * 
 *         This is a test entity to validate that we are able to manipulate
 *         extending resource entities with generic daos & services
 */
@Entity
public class DerivedEntity extends Resource {

	private static final long serialVersionUID = 3803263161030773997L;

	private String name;

	/**
	 * @return the entity name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the entity name
	 * 
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
