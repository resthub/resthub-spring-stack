package org.resthub.identity.service;

import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

public interface PermissionService {
	
	void addPermission(Object resource, long id, Sid recipient, Permission permission);
	void deletePermission(Object resource, long id, Sid recipient, Permission permission);
	Acl getPermissions(Object resource, long id, Sid recipient);
}
