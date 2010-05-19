package org.resthub.identity.dao.jpa;

import javax.inject.Named;

import org.resthub.core.dao.jpa.GenericJpaResourceDao;
import org.resthub.identity.dao.GroupDao;
import org.resthub.identity.model.Group;

@Named("groupDao")
public class JpaGroupDao extends GenericJpaResourceDao<Group> implements GroupDao {
    
}
