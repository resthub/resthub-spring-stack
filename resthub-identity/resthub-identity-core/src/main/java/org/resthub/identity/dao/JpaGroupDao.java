package org.resthub.identity.dao;

import javax.inject.Named;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.identity.model.Group;

/**
 * 
 * The JpaGroupDao is based on both {@link GenericJpaResourceDao} and
 * {@link GroupDao}<br/>
 * It is a bean whose name is "groupDao"
 * */
@Named("groupDao")
public class JpaGroupDao extends GenericJpaResourceDao<Group> implements
		GroupDao {

}
