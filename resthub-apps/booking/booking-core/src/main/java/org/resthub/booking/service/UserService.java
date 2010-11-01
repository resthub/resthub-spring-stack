package org.resthub.booking.service;

import org.resthub.booking.model.User;
import org.resthub.core.service.GenericService;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
public interface UserService extends GenericService<User, Long> {

	/**
	 * @param username
	 * @param password
	 * 
	 * @return the user if credentials are correct, else return null
	 */
	User checkCredentials(String username, String password);

	/**
	 * @param username
	 * 
	 * @return the user with this username if it exists, else return null
	 */
	User findByUsername(String username);

	/**
	 * @param email
	 * 
	 * @return the user with this email if it exists, else return null
	 */
	User findByEmail(String email);
}
