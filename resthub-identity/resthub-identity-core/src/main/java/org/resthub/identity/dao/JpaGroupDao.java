package org.resthub.identity.dao;

import javax.inject.Named;
import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.identity.model.Group;

@Named("groupDao")
public class JpaGroupDao extends GenericJpaResourceDao<Group> implements GroupDao {

}
