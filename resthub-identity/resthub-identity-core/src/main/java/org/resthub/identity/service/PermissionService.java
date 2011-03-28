package org.resthub.identity.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.identity.model.Group;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

@Named("permissionService")
public class PermissionService {
	
	private MutableAclService aclService;
	
	public MutableAclService getAclService() {
		return aclService;
	}

	@Inject
	@Named("aclService")
	public void setAclService(MutableAclService aclService) {
		this.aclService = aclService;
	}

	/**
	 * TODO : move to a PermissionService class
	 */
	public void addPermission(Group resource, Sid recipient, Permission permission) {
	       MutableAcl acl;
	       ObjectIdentity oid = new ObjectIdentityImpl(Group.class, resource.getId());
	 
	       try {
	           acl = (MutableAcl) aclService.readAclById(oid);
	       } catch (NotFoundException nfe) {
	           acl = aclService.createAcl(oid);
	       }
	 
	       acl.insertAce(acl.getEntries().size(), permission, recipient, true);
	       aclService.updateAcl(acl);

	}
	
	/**
	 * TODO : move to a PermissionService class
	 */
	public void deletePermission(Group resource, Sid recipient, Permission permission) {

		ObjectIdentity oid = new ObjectIdentityImpl(Group.class, resource.getId());
	       MutableAcl acl = (MutableAcl) aclService.readAclById(oid);

	       // Remove all permissions associated with this particular recipient (string equality to KISS)
	       List<AccessControlEntry> entries = acl.getEntries();

	       for (int i = 0; i < entries.size(); i++) {
	           if (entries.get(i).getSid().equals(recipient) && entries.get(i).getPermission().equals(permission)) {
	               acl.deleteAce(i);
	           }
	       }

	       aclService.updateAcl(acl);

	   }

}
