package org.resthub.identity.dao.jpa;

import javax.inject.Named;

import org.resthub.core.dao.jpa.GenericJpaResourceDao;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.User;

@Named("userDao")
public class JpaUserDao extends GenericJpaResourceDao<User> implements UserDao {
    
}
