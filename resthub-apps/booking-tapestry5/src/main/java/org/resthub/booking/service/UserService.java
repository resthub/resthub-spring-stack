package org.resthub.booking.service;

import org.resthub.booking.model.User;
import org.resthub.core.service.GenericService;


/**
 * Check credentials based on parameter
 * @return the user if credentials are correct, else return null
 */
public interface UserService extends GenericService<User, Long> {
	User checkCredentials(String username, String password);
	
	User findByUsername(String username);

	User findByEmail(String email);
}
