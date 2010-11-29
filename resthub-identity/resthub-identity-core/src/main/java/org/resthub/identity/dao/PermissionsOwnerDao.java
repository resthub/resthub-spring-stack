package org.resthub.identity.dao;

import org.resthub.core.dao.GenericResourceDao;
import org.resthub.identity.model.AbstractPermissionsOwner;
/**
 * This is the interface defining a PermissionsOwnerDao
 * It is based on a {@link GenericResourceDao} of type {@link AbstractPermissionsOwner}
 * */
public interface PermissionsOwnerDao<T extends AbstractPermissionsOwner> extends GenericResourceDao<T>{
	
	/**
	 * Finds a PermissionsOwnerDao by its id, <b>but with its groups loaded</b>
	 * 
	 * @param id The searched PermissionsOwnerDao id.
	 * @return The corresponding PermissionsOwnerDao (with its groups), or null if irrelevant.
	 */
	/** public T findByIdWithGroups(Long id);*/
	
}
