package org.resthub.identity.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.resthub.core.domain.model.Resource;

@Entity
public abstract class Identity extends Resource {

	private static final long serialVersionUID = 1L;

	protected List<String> permissions;

	public Identity() {
		super();
	}
	
	public Identity(String name) {
		super(name);
	}

	public Identity(String name, List<String> permissions) {
		super(name);
		this.permissions = permissions;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public void addPermission(String permission) {
		if (permissions == null)
			permissions = new ArrayList<String>();
		this.permissions.add(permission);
	}

	public void removePermission(String permission) {
		if (permissions != null) {
			permissions.remove(permission);
		}
	}

}
