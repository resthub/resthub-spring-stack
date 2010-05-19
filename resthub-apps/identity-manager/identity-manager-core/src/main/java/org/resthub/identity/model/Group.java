package org.resthub.identity.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Describe a group.
 */
@Entity
@Table(name = "UserGroup")
/* "Group" conflicts with SQL keyword */
@XmlRootElement
public class Group extends Identity {

	private static final long serialVersionUID = 475935404179730841L;

	protected String name = null;
	protected List<User> users = null;

	public Group() {
		super();
	}

	public Group(String name) {
		this.setName(name);
	}

	public Group(String name, List<String> permissions) {
		super(permissions);
		this.setName(name);
	}

	@Column(/* nullable = false */)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(mappedBy = "groups")
	@XmlElementWrapper(name = "users")
	@XmlElement(name = "user")
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public void addUser(User user) {
		if (users == null) {
			this.users = new ArrayList<User>();
		}
		this.users.add(user);
	}

	public void removeUser(String user) {
		if (users != null) {
			users.remove(user);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Group [");
		sb.append("Name: ").append(this.getName()).append(", ");
		sb.append("]");
		return sb.toString();
	}
}
