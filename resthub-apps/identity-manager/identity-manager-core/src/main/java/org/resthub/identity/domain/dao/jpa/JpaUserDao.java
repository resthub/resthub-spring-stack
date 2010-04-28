package org.resthub.identity.domain.dao.jpa;

import javax.inject.Named;

import org.resthub.core.domain.dao.jpa.GenericJpaResourceDao;
import org.resthub.identity.domain.dao.UserDao;
import org.resthub.identity.domain.model.User;

@Named("userDao")
public class JpaUserDao extends GenericJpaResourceDao<User> implements UserDao {
    
}
