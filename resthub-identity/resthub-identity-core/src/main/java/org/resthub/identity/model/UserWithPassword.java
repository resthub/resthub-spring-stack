package org.resthub.identity.model;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

@XmlRootElement
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
        
        public User toUser() {
            User user = new User();
            user.setEmail(this.getEmail());
            user.setFirstName(this.getFirstName());
            user.setId(this.getId());
            user.setLastName(this.getLastName());
            user.setLogin(this.getLogin());
            user.setPassword(this.getPassword());
            return user;
        }
}
