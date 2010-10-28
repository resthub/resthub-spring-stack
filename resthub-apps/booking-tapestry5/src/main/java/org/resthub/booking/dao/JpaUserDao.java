package org.resthub.booking.dao;

import javax.inject.Named;

import org.resthub.booking.model.User;
import org.resthub.core.dao.GenericJpaDao;


@Named("userDao")
public class JpaUserDao extends GenericJpaDao<User, Long> implements UserDao {

}
