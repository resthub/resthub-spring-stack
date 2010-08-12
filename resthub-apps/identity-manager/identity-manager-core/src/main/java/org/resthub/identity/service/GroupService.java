package org.resthub.identity.service;

import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.Group;

/**
 * Group services interface.
 * @author Guillaume Zurbach
 */
public interface GroupService extends GenericResourceService<Group> {

	/**
	 * Find user by login.
	 * @param login User login
	 * @return the user or null if more than one user is found
	 */
	public Group findByName( String name );
}
