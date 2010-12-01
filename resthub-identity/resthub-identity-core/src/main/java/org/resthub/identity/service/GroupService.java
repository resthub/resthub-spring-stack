package org.resthub.identity.service;

import java.util.List;

import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.Group;
import org.springframework.transaction.annotation.Transactional;

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
	
	/**
	 * Remove a group from one group's group
	 * 
	 * @param groupName
	 *            the name of the group to whom the groups should be removed
	 * @param subGroupName
	 *            the name of the group to remove
	
	 */
	public void removeGroupFromGroup(String groupName, String subGroupName);
	
	/**
	 * gets the Groups direct Permissions
	 * 
	 * @param groupName
	 *            the name of the Group
	 * @return permissions of the group.
	 */
	public List<String> getGroupDirectPermissions(String groupName);
	
	/**
	 * Add a permission to a group
	 * 
	 * @param groupName
	 *            the name of the group
	 * @param permission
	 *            the permission to be added
	 */
	public void addPermissionToGroup(String groupName, String permission);
	
	/**
	 * Remove the permission for the given group
	 * 
	 * @param groupName
	 *            the name of the group
	 * @param permission
	 *            the permission to delete
	 */
	public void removePermissionFromGroup(String groupName, String permission);

	/**
	 * Add a group from one group's groups
	 * 
	 * @param groupName
	 *            the name of the group to whom to group should be added
	 * @param subGroupName
	 *            the name of the group to add from the group's group list
	 */
	@Transactional
	public void addGroupToGroup(String groupName, String subGroupName);

}
