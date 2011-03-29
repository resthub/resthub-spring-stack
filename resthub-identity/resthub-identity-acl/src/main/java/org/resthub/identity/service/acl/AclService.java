package org.resthub.identity.service.acl;

import org.resthub.core.model.Resource;
import org.springframework.security.acls.model.Acl;

/**
 * Service that manage ACLs.
 * An ACL (Access Control Layer) is the combination of a domain object, a permission and
 * a user's id.
 * TODO
 */
public interface AclService {
	
	/**
	 * Saves an ACL (or update the existing one) for a specified owner on a specified resource.
	 *  
	 * @param domainObject The resource concerned.
	 * @param userId Id of the user.
	 * @param permission Permission added to this owner on this resource.
	 */
	void saveAcl(Resource domainObject, String userId, String permission);
	
	/**
	 * Removes an ACL for a specified owner on a specified resource.
	 * 
	 * @param domainObject The resource concerned.
	 * @param userId Id of the user.
	 * @param permission Permission removed from this owner on this resource.
	 * 
	 * @throws NotFoundException If the permission is not associated to this owner on 
	 */
	void removeAcl(Resource domainObject, String userId, String permission);
	
	/**
	 * Retrieves existing ACL for a specified resource.
	 * 
	 * @param domainObject The resource concerned.
	 * @return ACL for this resource, containing all permissions of all related owner.
	 */
	Acl getAcls(Resource domainObject);
	
} // interface AclService
