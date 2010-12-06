package org.resthub.identity.dao;

import org.resthub.core.dao.GenericResourceDao;
import org.resthub.identity.model.AbstractPermissionsOwner;
/**
 * This is the interface defining a PermissionsOwnerDao
 * It is based on a {@link GenericResourceDao} of type {@link AbstractPermissionsOwner}
 * */
public interface PermissionsOwnerDao<T extends AbstractPermissionsOwner> extends GenericResourceDao<T>{
	

}
