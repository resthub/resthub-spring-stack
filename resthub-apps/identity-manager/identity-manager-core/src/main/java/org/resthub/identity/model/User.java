package org.resthub.identity.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Describe a user account.
 */
@Entity
@Table
@XmlRootElement
public class User extends Identity {

	private static final long serialVersionUID = -7139715798005612136L;

	protected String login = null;
	protected String password = null;
	protected String email = null;
	protected List<Group> groups = null;

	public User() {
		super();
	}

	public User(String login) {
		this.setLogin(login);
	}

	public User(String login, List<String> permissions) {
		super(permissions);
		this.setLogin(login);
	}

	@Column(/* nullable = false */)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(/* nullable = false */)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(/* nullable = false */)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	/*
	 * @XmlElementWrapper(name="groups")
	 *
	 * @XmlElement(name="group")
	 */
	@ManyToMany
	@JoinTable(name = "user_group")
	@XmlTransient
	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	/**
	 * Add a user in a group. Then, it is necessary to give the group rights to
	 * the user (service layer).
	 */
	public void addGroup(Group group) {
		if (groups == null) {
			this.groups = new ArrayList<Group>();
		}
		this.groups.add(group);
	}

	public void removeGroup(String group) {
		if (groups != null) {
			groups.remove(group);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("User [");
		sb.append("Login: ").append(this.getLogin()).append(", ");
		sb.append("Email: ").append(this.getEmail()).append(", ");
		sb.append("]");
		return sb.toString();
	}

}
