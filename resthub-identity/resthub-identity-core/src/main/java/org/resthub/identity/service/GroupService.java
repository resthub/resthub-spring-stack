package org.resthub.identity.service;

import java.util.List;

import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.Group;

/**
 * Group services interface.
 * @author Guillaume Zurbach
 */
public interface GroupService extends GenericResourceService<Group> {

	/**
	 * Finds group by name.
	 * @param name
	 * 		the group's Name
	 * @return the group or null if no group with this name is found
	 */
	public Group findByName( String name );

	/**
	 * Find all groups available.
	 * @return a list of groups.
	 */
	public List<Group> findAllGroups();

}
