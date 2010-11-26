package org.resthub.identity.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.resthub.core.model.Resource;

/**
 * <p>Describe a generic class for users and groups. It contains/manage mainly
 * permissions. When possible you SHOULD use method form the class and not from
 * the permissions list</p>
 */
@Entity
@Table(name="PermissionsOwner")
@XmlRootElement
public abstract class AbstractPermissionsOwner extends Resource {

	private static final long serialVersionUID = 3248710006663061799L;

	/**
	 * the list of permissions
	 * */
	protected List<String> permissions = new ArrayList<String>();

	/**
	 * List of Permissions Owner (Group) in which the Permissions Owner is
	 * */
	protected List<Group> groups = new ArrayList<Group>();
	
	/** Default constructor */
	public AbstractPermissionsOwner() {

	}

	public AbstractPermissionsOwner(AbstractPermissionsOwner i){
		List<String> pPermissions=i.getPermissions();
		permissions.addAll(pPermissions);
	}
	
	/**
	 * Constructor
	 * 
	 * @param permissions
	 *            list of permission to be assigned to the new Identity
	 * */
	public AbstractPermissionsOwner(List<String> permissions) {
		this.permissions = permissions;
	}

	/**
	 * Retrieve the permissions assigned to the identity
	 * 
	 * @return the permissions list if permissions have been assigned, otherwise
	 *         null
	 * */
	@ElementCollection(fetch = FetchType.EAGER)
	@XmlElementWrapper(name = "permissions")
	@XmlElement(name = "permission")
	public List<String> getPermissions() {
		return permissions;
	}

	/**
	 * Define the list of permission of the identity
	 * 
	 * @param permissions
	 *            , the list of permission to be assigned
	 * */
	private void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	
	/**
	 * gets the user's Groups
	 * 
	 * @return the list of groups in which the user is. The List could be null
	 *         is the user is in no group
	 * 
	 */
	@ManyToMany
	@JoinTable(name = "PermissionsOwner_group")
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 * sets the Groups in which the user is
	 * 
	 * @param groups
	 *            the list of groups in which the user belongs
	 * */
	private void setGroups(List<Group> groups) {
		this.groups = groups;
	}

}