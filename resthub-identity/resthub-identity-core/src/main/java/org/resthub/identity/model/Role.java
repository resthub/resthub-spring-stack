package org.resthub.identity.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.resthub.core.model.Resource;

@Entity
@Table(name = "idm_roles")
@XmlRootElement
@Indexed
public class Role extends Resource {
	private static final long serialVersionUID = 4727979823727123519L;

	protected String name;
	protected List<String> permissions = new ArrayList<String>();

	/**
	 * Hide default constructor not to be able to create roles w/o a name.
	 */
	protected Role() {
	}
	
	/**
	 * @param roleName Name of the role to create.
	 */
	public Role(String roleName) {
		this.setName(roleName);
	}
	
	/**
	 * Retrieve the name of the role.
	 * 	
	 * @return the name of the role.
	 */
	@Field
	@Column(nullable = false, unique = true)
	public String getName() {
		return this.name;
	}
	
	/**
	 * Change the name of the role.
	 * 
	 * @param roleName Name of the role.
	 */
	public void setName(String roleName) {
		this.name = roleName;
	}
	
	/**
	 * Retrieve the permissions assigned to the role.
	 * 
	 * @return the permissions assigned to the role.
	 * */
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "role_permission")
	@XmlElementWrapper(name = "permissions")
	@XmlElement(name = "permission")
	public List<String> getPermissions() {
		return permissions;
	}

	/**
	 * Define the list of permissions of the role.
	 * 
	 * @param permissions List of permissions to assign to the role.
	 * */
	protected void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
}
