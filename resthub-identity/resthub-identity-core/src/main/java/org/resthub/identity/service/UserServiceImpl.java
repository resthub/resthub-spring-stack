package org.resthub.identity.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.annotation.Auditable;
import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.resthub.oauth2.provider.service.AuthenticationService;
import org.springframework.util.Assert;

@Named("userService")
public class UserServiceImpl extends GenericResourceServiceImpl<User, UserDao> implements UserService, AuthenticationService{

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

	@Override
	public String getUser(String userName, String password) {
		User u = this.authenticateUser(userName, password);
		return (u!=null)? u.getLogin() : null;
		}

	@Override
	public List<String> getUserPermissions(String userId) {
		List<String> userPermissisons = new ArrayList<String>();
		userPermissisons.addAll(this.findByLogin(userId).getPermissions());
		for( Group g : this.findByLogin(userId).getGroups()){
			userPermissisons.addAll(g.getPermissions());
		}
		return userPermissisons;
	}

}
