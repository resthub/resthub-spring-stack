package org.resthub.identity.model;

import javax.persistence.Column;

import org.codehaus.jackson.annotate.JsonIgnore;

public class UserWithPassword extends User {

	public UserWithPassword() {
		super();
	}

	public UserWithPassword(User u) {
		super(u);
	}

	
	@Override
	@Column(nullable = false)
	@JsonIgnore(value=false)
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public void setPassword(String password) {
		super.setPassword(password);
	}

}
