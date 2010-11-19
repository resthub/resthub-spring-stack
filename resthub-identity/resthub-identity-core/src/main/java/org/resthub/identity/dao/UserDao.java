package org.resthub.identity.dao;

import org.resthub.core.dao.GenericResourceDao;
import org.resthub.identity.model.User;

public interface UserDao extends GenericResourceDao<User> {

	/**
	 * Authenticate the User based on login and password and return the User
	 * 
	 * @param login
	 * @param password
	 * @return the user or null if no user found with such login and password
	 */
	User getUserByAuthenticationInformation(String login, String password);
}
