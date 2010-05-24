package org.resthub.identity.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.User;

@Named("userService")
public class UserServiceImpl extends GenericResourceServiceImpl<User, UserDao> implements UserService {

	@Inject
	@Named("userDao")
	public void setResourceDao(UserDao userDao) {
		super.setDao(userDao);
	}

}
