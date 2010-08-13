package org.resthub.identity.dao;

import javax.inject.Named;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.identity.model.User;

@Named("userDao")
public class JpaUserDao extends GenericJpaResourceDao<User> implements UserDao {

}