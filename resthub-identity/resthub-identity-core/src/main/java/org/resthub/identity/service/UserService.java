package org.resthub.identity.service;

import java.util.List;
import org.resthub.core.exception.AlreadyExistingEntityException;

import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.service.tracability.TracableService;
import org.springframework.transaction.annotation.Transactional;

/**
 * User services interface.
 * 
 * @author Guillaume Zurbach
 */
public interface UserService extends GenericResourceService<User>, TracableService {

    /**
     * Kind of changes notified by this service
     */
    enum UserServiceChange {

        /**
         * User creation. Arguments : 
         * 1- created user.
         */
        USER_CREATION,
        /**
         * User deletion. Arguments : 
         * 1- deleted user.
         */
        USER_DELETION,
        /**
         * User addition to a group. Arguments : 
         * 1- added user.
         * 2- concerned group.
         */
        USER_ADDED_TO_GROUP,
        /**
         * User removal from a group. Arguments : 
         * 1- removed user.
         * 2- concerned group.
         */
        USER_REMOVED_FROM_GROUP
    };

    /**
     * Create a new user.
     * @param user User to create
     * @return new user
     */
    @Override
    User create(User user) throws AlreadyExistingEntityException;

    /**
     * Update existing user.
     * @param user User to update
     * @return user updated
     */
    @Override
    User update(User user) throws AlreadyExistingEntityException;

    /**
     * Find user by login.
     *
     * @param login
     *            User login
     * @return the user or null if more than one user is found
     */
    public User findByLogin(String login);

    /**
     * Update the password for the given user
     *
     * @param user
     *            the user to with the new password
     */
    public User updatePassword(User user);

    /**
     * Authenticate the user with Login and password
     *
     * @param login
     * @param password
     * @return the authenticated user or null if no user found with such login
     *         and password
     */
    public User authenticateUser(String login, String password);

    /**
     * Remove a group from one user's groups
     *
     * @param userLogin
     *            the login of the user to whom to group should be remove
     * @param groupeName
     *            the name of the group to remove from the user's group list
     */
    public void removeGroupFromUser(String userLogin, String groupName);

    /**
     * gets the User's inherited Permissions
     *
     * @param login
     *            the login of the user
     * @return permissions of the user. In this implementation inherited
     *         permissions from group to which the user belong are taken into
     *         accounts
     */
    public List<String> getUserPermissions(String login);

    /**
     * gets the User's direct Permissions
     *
     * @param login
     *            the login of the user
     * @return permissions of the user.
     */
    public List<String> getUserDirectPermissions(String login);

    /**
     * Add a permission to an user
     *
     * @param userLogin
     *            the login of the user
     * @param permission
     *            the permission to be added
     */
    public void addPermissionToUser(String userLogin, String permission);

    /**
     * Remove the permission for the given user
     *
     * @param userLogin
     *            the login of the user
     * @param permission
     *            the permission to delete
     */
    public void removePermissionFromUser(String userLogin, String permission);

    /**
     * Add a group from one user's groups
     *
     * @param userLogin
     *            the login of the user to whom to group should be added
     * @param groupeName
     *            the name of the group to add from the user's group list
     */
    @Transactional
    public void addGroupToUser(String userLogin, String groupName);

    /**
     * Gets the user of a group.
     * @param groupName The name of the group.
     * @return A list of users corresponding to the given group.
     */
    List<User> getUsersFromGroup(String groupName);

    /**
     * Gets all the users that have a role, direct or inherited.
     * @param roles A list of roles to look for.
     * @return A list of users having at least one of the roles defined as parameter.
     */
    List<User> findAllUsersWithRoles(List<String> roles);

    /**
     * Add a role to a user.
     * @param userLogin User to which the role will be added.
     * @param roleName The role that will be added.
     */
    void addRoleToUser(String userLogin, String roleName);

    /**
     * Remove a role from a user.
     * @param userLogin User to which the role will be removed.
     * @param roleName The role that will be removed.
     */
    void removeRoleFromUser(String userLogin, String roleName);

    /**
     * Get all the roles a user owns.
     * 
     * @param userLogin User to gather roles from.
     * @return A list of roles that the given user has.
     */
    List<Role> getAllUserRoles(String userLogin);
}
