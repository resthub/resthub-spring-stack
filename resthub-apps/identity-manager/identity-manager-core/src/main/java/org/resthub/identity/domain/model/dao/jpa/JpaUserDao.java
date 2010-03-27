package org.resthub.identity.domain.model.dao.jpa;

import javax.inject.Named;

import org.resthub.core.domain.dao.jpa.AbstractJpaResourceDao;
import org.resthub.identity.domain.model.User;
import org.resthub.identity.domain.model.dao.UserDao;

@Named("userDao")
public class JpaUserDao extends AbstractJpaResourceDao<User> implements UserDao {
    
}
