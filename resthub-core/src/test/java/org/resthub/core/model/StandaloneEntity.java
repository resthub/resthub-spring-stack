package org.resthub.core.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author bmeurant <baptiste.meurant@gmail.com>
 * 
 *         This is a test entity to validate that we are able to manipulate non
 *         extending resource entities with generic daos & services
 */
@Entity
public class StandaloneEntity {

	private Long id;
	private String name;

	/**
	 * Get the id.
	 * 
	 * @return id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Set the id.
	 * 
	 * @param id
	 *            id
	 */
	public void setId(Long id) {
		this.id = id;
	}

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
