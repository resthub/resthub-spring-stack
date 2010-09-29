package org.resthub.synchro.dao.couchdb;

import org.resthub.couchdb.dao.CouchDBDao;

public class UserDao extends CouchDBDao<User> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<User> getManagedClass() {
		return User.class;
	} // setClass().
	
	/**
	 * Retrieves id of users.
	 * 
	 * @param user User from whom we need id (may be null).
	 * @return the user's id (may be null for new users and null arguments).
	 */
	@Override
	protected String getId(User user) {
		return user == null ? null : user.getId();
	} // getId().

}
