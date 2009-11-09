package org.resthub.identity.domain.model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.jcrom.annotations.JcrProperty;
import org.jcrom.annotations.JcrReference;

public class User extends Entity implements Principal {

	private static final long serialVersionUID = 1L;

	@JcrProperty
	protected String password = null;
	
	@JcrProperty
	protected String email = null;
	
	@JcrReference(byPath=true)
	protected List<Group> groups = null;

	public User() {
		super();
	}

	public User(String name) {
		super(name);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	public void addGroup(Group group) {
		if (groups == null)
			groups = new ArrayList<Group>();
		this.groups.add(group);
	}

	public void removeGroup(Group group) {
		if (groups != null) {
			groups.remove(group);
		}
	}
	
	public List<String> getUserAndGroupsPermissions() {
		List<String> userAndGroupsPermissions = new ArrayList<String>();
		if(this.getPermissions()!= null) {
			userAndGroupsPermissions.addAll(this.getPermissions());
		}
		if(this.groups != null) {
			for(Group group : this.groups) {
				if(group.getPermissions() != null) {
					userAndGroupsPermissions.addAll(group.getPermissions());
				}
			}
		}
		return userAndGroupsPermissions;
	}

}
