package org.resthub.identity.domain.model;

import javax.persistence.Entity;


@Entity
public class Group extends Identity {

	private static final long serialVersionUID = 1L;

	public Group() {
		super();
	}

	public Group(String name) {
		super(name);
	}

}
