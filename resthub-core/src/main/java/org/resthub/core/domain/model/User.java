package org.resthub.core.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
public class User extends Resource {

	protected String password = null;
	
	protected String email = null;
	
	public User() {
		super();
	}

	public User(String name) {
		super(name);
	}

	@Column
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User [");
        sb.append("Name: ").append(this.getName()).append(", ");
        sb.append("]");
        return sb.toString();
    }

}
