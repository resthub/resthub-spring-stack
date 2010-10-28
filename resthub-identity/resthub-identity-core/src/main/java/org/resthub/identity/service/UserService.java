package org.resthub.identity.service;

import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.User;

/**
 * User services interface.
 * @author Guillaume Zurbach
 */
public interface UserService extends GenericResourceService<User> {

	/**
	 * Find user by login.
	 * @param login User login
	 * @return the user or null if more than one user is found
	 */
	public User findByLogin( String login );

	/*
	 * Find user by login and password
	 * @param login, password
	 * @return the user or null if no user found with such login and password
	 */
	public User authenticateUser(String login, String password);
}
