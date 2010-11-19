package org.resthub.identity.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table
@XmlRootElement
public class Identity extends Resource {

	private static final long serialVersionUID = 3248710006663061799L;

	/**
	 * the list of permissions
	 * */
	protected List<String> permissions = null;

	/** Default constructor */
	public Identity() {

	}

	/**
	 * Constructor
	 * 
	 * @param permissions
	 *            list of permission to be assigned to the new Identity
	 * */
	public Identity(List<String> permissions) {
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
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	/**
	 * Add a permission to the list of permission If the permission is already
	 * present, nothing is done
	 * 
	 * @param permission
	 *            , the permission to be added
	 * */
	public void addPermission(String permission) {
		if (permissions == null) {
			this.permissions = new ArrayList<String>();
		}
		if (!this.permissions.contains(permission)) {
			this.permissions.add(permission);
		}
	}

	/**
	 * Remove a permission from the permission list if the permission is present
	 * many times, all elements corresponding to the permissions will be deleted
	 * 
	 * @param permission
	 *            the permission to be remove
	 * */
	public void removePermission(String permission) {
		if (permissions != null) {
			while (permissions.remove(permission))
				;
		}
	}

	/**
	 * check if a permission if given to an identity
	 * 
	 * @param permission
	 *            the string corresponding to the permission that we are looking
	 *            for
	 * @return boolean, true is the permission is in the permission list,false
	 *         otherwise
	 * */
	public boolean permissionListContains(String permission) {
		boolean b = false;
		if (this.permissions != null) {
			b = this.permissions.contains(permission);
		}
		return b;
	}
}