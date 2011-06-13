package org.resthub.identity.service;

import java.util.List;

import org.resthub.core.exception.AlreadyExistingEntityException;
import org.resthub.core.service.GenericService;
import org.resthub.identity.model.Group;
import org.resthub.identity.service.tracability.TracableService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Group services interface.
 * 
 * @author Guillaume Zurbach
 */
public interface GroupService extends GenericService<Group, Long>, TracableService {

    /**
     * Kind of changes notified by this service
     */
    enum GroupServiceChange {

        /**
         * Group creation. Arguments : 1- created group.
         */
        GROUP_CREATION,
        /**
         * Group deletion. Arguments : 1- deleted group.
         */
        GROUP_DELETION,
        /**
         * Group addition to a group. Arguments : 1- added group. 2- concerned
         * parent group.
         */
        GROUP_ADDED_TO_GROUP,
        /**
         * Group removal from a group. Arguments : 1- removed group. 2-
         * concerned parent group.
         */
        GROUP_REMOVED_FROM_GROUP
    };

    /**
     * Create a new group.
     * 
     * @param group
     *            Group to create
     * @return new group
     */
    @Override
    Group create(Group group) throws AlreadyExistingEntityException;

    /**
     * Update existing group.
     * 
     * @param group
     *            Group to update
     * @return group updated
     */
    @Override
    Group update(Group group) throws AlreadyExistingEntityException;

    /**
     * Finds group by name.
     * 
     * @param name
     *            the group's Name
     * @return the group or null if no group with this name is found
     */
    public Group findByName(String name);

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

    /**
     * Add a role to a group.
     * 
     * @param groupName
     *            Group to which the role will be added.
     * @param roleName
     *            The role that will be added.
     */
    void addRoleToGroup(String groupName, String roleName);

    /**
     * Remove a role from a group.
     * 
     * @param groupName
     *            Group to which the role will be removed.
     * @param roleName
     *            The role that will be removed.
     */
    void removeRoleFromGroup(String groupName, String roleName);
}
