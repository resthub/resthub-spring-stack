package org.resthub.identity.dao;

import javax.inject.Named;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.identity.model.User;

/**
 * The JpaUserDao class is based on both {@link GenericJpaResourceDao} and
 * {@link UserDao}<br/>
 * 
 * It is a bean whose name is userDao
 * */
@Named("userDao")
public class JpaUserDao extends GenericJpaResourceDao<User> implements PermissionsOwnerDao<User> {

}