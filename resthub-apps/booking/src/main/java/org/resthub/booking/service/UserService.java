package org.resthub.booking.service;

import org.resthub.booking.model.User;
import org.resthub.core.service.GenericResourceService;

/**
 * Check credentials based on parameter
 * @return the user if credentials are correct, else return null
 */
public interface UserService extends GenericResourceService<User> {
	User checkCredentials(String username, String password);
}
