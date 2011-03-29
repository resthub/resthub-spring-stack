package org.resthub.identity.service.acl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.model.Resource;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

/**
 * Implementation of the ACL Service based on Spring Security.
 */
@Named("idmAclService")
public class AclServiceImpl implements AclService {
	
	// -----------------------------------------------------------------------------------------------------------------
	// Protected attributes
	
	/**
	 * Spring Security's ACL facility. Injected by Spring.
	 */
	@Inject
	@Named("aclService")
	protected MutableAclService aclService;
	
	/**
	 * Mapper between strings and permissions. Injected by Spring.
	 */
	@Inject
	protected ConfigurablePermissionFactory permissionFactory;
	
	// -----------------------------------------------------------------------------------------------------------------
	// Inherited methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveAcl(Resource domainObject, String ownerId, String permission) {
		// Sid identifies the user.
		Sid owner = new PrincipalSid(ownerId); 
		// ObjectIdentity is a unic identifier for the model object
		ObjectIdentity oid = new ObjectIdentityImpl(domainObject.getClass(), domainObject.getId());
		
		// Creates the acl, or update the existing one.
		MutableAcl acl;
	    try {
	    	acl = (MutableAcl) aclService.readAclById(oid);
	    } catch (NotFoundException nfe) {
	    	acl = aclService.createAcl(oid);
	    }
	    
	    // Update the acl for this user on this model object.
	    acl.insertAce(acl.getEntries().size(), permissionFactory.buildFromName(permission), owner, true);
	    aclService.updateAcl(acl);
	} // saveAcl().
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAcl(Resource domainObject, String ownerId, String permission) {
		// Sid identifies the user.
		Sid owner = new PrincipalSid(ownerId); 
		// ObjectIdentity is a unic identifier for the model object
		ObjectIdentity oid = new ObjectIdentityImpl(domainObject.getClass(), domainObject.getId());
		
		// Gets the existing acl.
		MutableAcl acl = (MutableAcl) aclService.readAclById(oid);
		Permission effectivePerm = permissionFactory.buildFromName(permission);
		
		// Remove all permissions associated with this particular recipient (string equality to KISS)
	    List<AccessControlEntry> entries = acl.getEntries();
	    int i = 0;
	    for (AccessControlEntry entry : entries) {
        	if (entry.getSid().equals(owner) && entry.getPermission().equals(effectivePerm)) {
               acl.deleteAce(i);
        	}
        	i++;
        }

		// Update the acl for this user on this model object.
	    aclService.updateAcl(acl);		
	} // removeAcl().
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Acl getAcls(Resource domainObject) {
		// ObjectIdentity is a unic identifier for the model object
		ObjectIdentity oid = new ObjectIdentityImpl(domainObject.getClass(), domainObject.getId());
		MutableAcl acl = (MutableAcl) aclService.readAclById(oid);
		return acl;
	} // getAcl().

} // class AclServiceImpl.
