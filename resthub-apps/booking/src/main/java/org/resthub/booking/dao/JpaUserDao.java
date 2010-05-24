package org.resthub.booking.dao;

import javax.inject.Named;
import org.resthub.booking.model.User;
import org.resthub.core.dao.GenericJpaResourceDao;

@Named("userDao")
public class JpaUserDao extends GenericJpaResourceDao<User> {

}
