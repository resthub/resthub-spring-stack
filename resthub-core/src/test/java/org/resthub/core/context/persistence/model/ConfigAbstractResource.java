package org.resthub.core.context.persistence.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ConfigAbstractResource {

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
