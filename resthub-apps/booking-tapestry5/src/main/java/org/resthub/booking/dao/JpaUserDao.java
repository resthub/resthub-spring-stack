package org.resthub.booking.dao;

import javax.inject.Named;

import org.resthub.booking.model.User;
import org.resthub.core.dao.GenericJpaDao;

/**
 * @author Guillaume Zurbach
 * @author Baptiste Meurant
 */
@Named("userDao")
public class JpaUserDao extends GenericJpaDao<User, Long> implements UserDao {

}
