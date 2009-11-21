package org.resthub.identity.domain.model;

import javax.persistence.Table;

@javax.persistence.Entity
@Table(name="GROUP")
public class Group extends Entity {

	private static final long serialVersionUID = 1L;

	public Group() {
		super();
	}

	public Group(String name) {
		super(name);
	}

}
