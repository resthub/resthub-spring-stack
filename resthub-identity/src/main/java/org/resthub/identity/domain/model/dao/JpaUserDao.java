package org.resthub.identity.domain.model.dao;

import javax.inject.Named;

import org.resthub.core.domain.dao.jpa.AbstractJpaResourceDao;
import org.resthub.identity.domain.model.User;

@Named("userDao")
public class JpaUserDao extends AbstractJpaResourceDao<User> {
    
}
