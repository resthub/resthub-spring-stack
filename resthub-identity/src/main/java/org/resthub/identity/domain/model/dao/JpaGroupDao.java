package org.resthub.identity.domain.model.dao;

import org.resthub.core.domain.dao.jpa.JpaResourceDao;
import org.resthub.identity.domain.model.Group;
import org.springframework.stereotype.Repository;

@Repository("groupDao")
public class JpaGroupDao extends JpaResourceDao<Group> {

}
