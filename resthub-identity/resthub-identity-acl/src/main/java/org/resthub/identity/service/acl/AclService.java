package org.resthub.identity.service.acl;

import java.io.Serializable;
import java.util.List;

import org.resthub.identity.service.tracability.TracableService;
import org.springframework.security.acls.model.Acl;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service that manage ACLs.
 * An ACL (Access Control Layer) is the combination of a domain object, a permission and
 * a user's id.
 * TODO
 */
@Transactional(readOnly=false)
public interface AclService extends TracableService {
	
	/**
	 * Kind of changes notified by this service
	 */
	enum AclServiceChange {
		/**
		 * Group creation. Arguments : 
		 * 1- Id of the concerned domainObject
		 * 2- Id of the concerned user
		 * 3- added permission
		 */
		ACL_CREATION, 
		/**
		 * Group deletion. Arguments : 
		 * 1- Id of the concerned domainObject
		 * 2- Id of the concerned user
		 * 3- added permission
		 */
		ACL_DELETION
	};
	
	/**
	 * Saves an ACL (or update the existing one) for a specified owner on a specified resource.
	 *  
	 * @param domainObject The resource concerned.
	 * @param domainObjectId The concerned resource Id.
	 * @param userId Id of the user.
	 * @param permission Permission added to this owner on this resource.
	 */
	void saveAcl(Object domainObject, Serializable domainObjectId, String userId, String permission);
	
	/**
	 * Removes an ACL for a specified owner on a specified resource.
	 * 
	 * @param domainObject The resource concerned.
	 * @param domainObjectId The concerned resource Id.
	 * @param userId Id of the user.
	 * @param permission Permission removed from this owner on this resource.
	 * 
	 * @throws NotFoundException If the permission is not associated to this owner on 
	 */
	void removeAcl(Object domainObject, Serializable domainObjectId, String userId, String permission);
	
	/**
	 * Retrieves existing ACL for a specified resource.
	 * 
	 * @param domainObject The resource concerned.
	 * @param domainObjectId The concerned resource Id.
	 * @return ACL for this resource, containing all permissions of all related owner.
	 */
	@Transactional(readOnly=true)
	Acl getAcls(Object domainObject, Serializable domainObjectId);
	
	/**
	 * Saves an ACL (or update the existing one) for a specified owner on a specified resource.
	 *  
	 * @param domainObject The resource concerned.
	 * @param domainObjectId The concerned resource Id.
	 * @param userId Id of the user.
	 * @param permissions List of permissions added to this owner on this resource.
	 */
	void saveAcls(Object domainObject, Serializable domainObjectId, String userId, List<String> permissions);
	
	/**
	 * Removes an ACL for a specified owner on a specified resource.
	 * 
	 * @param domainObject The resource concerned.
	 * @param domainObjectId The concerned resource Id.
	 * @param userId Id of the user.
	 * @param permissions List of permissions removed from this owner on this resource.
	 * 
	 * @throws NotFoundException If the permission is not associated to this owner on 
	 */
	void removeAcls(Object domainObject, Serializable domainObjectId, String userId, List<String> permissions);
	
} // interface AclService
