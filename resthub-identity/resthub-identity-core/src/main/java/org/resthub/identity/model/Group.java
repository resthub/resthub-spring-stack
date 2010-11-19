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
 * Describes a group.<br/>
 * A group has few attributes, a name, a list of {@link User} belonging to this
 * Group and some permissions
 */
@Entity
@Table(name = "UsersGroup")
/* "Group" conflicts with SQL keyword */
@XmlRootElement
public class Group extends Identity {

	private static final long serialVersionUID = 475935404179730841L;
	/**
	 * name of the group
	 * */
	protected String name = null;

	/**
	 * List of users in the group
	 * */
	protected List<User> users = null;

	/**
	 * Default Constructor
	 * */
	public Group() {

	}

	/**
	 * getName
	 * 
	 * @return name of the group
	 * */
	@Column(unique = true, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * setName
	 * 
	 * @param name
	 *            the name to be set for the group
	 * */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getUsers
	 * 
	 * @return the list of users belonging to the group
	 * 
	 * */
	@ManyToMany(mappedBy = "groups")
	@XmlElementWrapper(name = "users")
	@XmlElement(name = "user")
	public List<User> getUsers() {
		return users;
	}

	/**
	 * set all {@link User} belonging to this Group
	 * 
	 * @param users
	 *            , List<Users> , the list of users to be assign to the group
	 * */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * Add a User in a Group
	 * 
	 * @param user
	 *            , the user to be added
	 * */
	public void addUser(User user) {
		if (users == null) {
			this.users = new ArrayList<User>();
		}
		this.users.add(user);
	}

	/**
	 * Remove an user from the group
	 * 
	 * @param user
	 *            the user to be removed
	 * */
	public void removeUser(User user) {
		if (users != null) {
			users.remove(user);
		}
	}

	/**
	 * Display the representation of the group Display the ID and the
	 * Name of the Group
	 * */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Group[");
		sb.append("Id: ").append(this.getId()).append(", ");
		sb.append("Name: ").append(this.getName());
		sb.append("]");
		return sb.toString();
	}
}
