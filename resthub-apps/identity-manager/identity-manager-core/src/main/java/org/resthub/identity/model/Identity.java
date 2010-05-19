package org.resthub.identity.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


import org.resthub.core.model.Resource;

/**
 * Describe a generic class for users and groups. It contains/manage mainly
 * permissions.
 */
@Entity
@Table
@XmlRootElement
public class Identity extends Resource {

	private static final long serialVersionUID = 3248710006663061799L;

	protected List<String> permissions = null;

	public Identity() {
		super();
	}

	public Identity(List<String> permissions) {
		this.setPermissions(permissions);
	}

	@ElementCollection(fetch=FetchType.EAGER)
	@XmlElementWrapper(name = "permissions")
	@XmlElement(name = "permission")
	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public void addPermission(String permission) {
		if (permissions == null) {
			this.permissions = new ArrayList<String>();
		}
		this.permissions.add(permission);
	}

	public void removePermission(String permission) {
		if (permissions != null) {
			permissions.remove(permission);
		}
	}
}