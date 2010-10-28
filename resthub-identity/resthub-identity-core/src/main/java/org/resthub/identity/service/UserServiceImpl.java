package org.resthub.identity.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.annotation.Auditable;
import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.User;
import org.springframework.util.Assert;

@Named("userService")
public class UserServiceImpl extends GenericResourceServiceImpl<User, UserDao> implements UserService {

	@Inject
	@Named("userDao")
	public void setResourceDao(UserDao userDao) {
		super.setDao(userDao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	public User findByLogin(String login) {
		Assert.notNull(login, "User login can't be null");
		List<User> result = this.dao.findEquals("login", login);

		if( result.size() == 1 ) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public User authenticateUser(String login, String password) {
		return dao.getUserByAuthenticationInformation(login,password);		
	}


}
