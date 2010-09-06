package org.resthub.identity.service;

import java.util.List;
import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;

/**
 * Group services interface.
 * @author Guillaume Zurbach
 */
public interface GroupService extends GenericResourceService<Group> {

	/**
	 * Find user by login.
	 * @param login User login
	 * @return the user or null if no user is found or if multiple users are found
	 */
	public Group findByName( String name );

	/**
	 * Find all groups available.
	 * @return a list of groups.
	 */
	public List<Group> findAllGroups();

	/**
	 * Remove a user from a group.
	 * @param group
	 * @param user
	 */
	public void removeUser( Group group, User user );
}
