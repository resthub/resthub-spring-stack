package org.resthub.identity.dao;

import javax.inject.Named;

import org.resthub.core.dao.GenericJpaDao;
import org.resthub.identity.model.Group;

/**
 * 
 * The JpaGroupDao is based on both {@link GenericJpaResourceDao} and
 * {@link GroupDao}<br/>
 * It is a bean whose name is "groupDao"
 * */
@Named("groupDao")
public class JpaGroupDao extends GenericJpaDao<Group, Long> implements
		PermissionsOwnerDao<Group> {

}
