package org.resthub.identity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
public class Group extends AbstractPermissionsOwner {

	private static final long serialVersionUID = 475935404179730841L;
	/**
	 * name of the group
	 * */
	protected String name = null;


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
	 * Display the representation of the group Display the ID and the Name of
	 * the Group
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
