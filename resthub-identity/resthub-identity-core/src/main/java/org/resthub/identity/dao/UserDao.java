package org.resthub.identity.dao;

import org.resthub.core.dao.GenericResourceDao;
import org.resthub.identity.model.User;

public interface UserDao extends GenericResourceDao<User> {

	/**
	 * Finds a users by its id, <b>but with its groups loaded</b>
	 * 
	 * @param id The searched user id.
	 * @return The corresponding user (with its groups), or null if irrelevant.
	 */
	public User findByIdWithGroups(Long id);
}
