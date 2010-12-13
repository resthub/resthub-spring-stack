package org.resthub.core.context.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class ConfigResourceFour {

	private static final long serialVersionUID = -6397995008582106415L;
	
	private Long id;

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

}
