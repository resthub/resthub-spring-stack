package org.resthub.identity.service;

import java.util.List;

import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * User services interface.
 * 
 * @author Guillaume Zurbach
 */
public interface UserService extends GenericResourceService<User> {

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
}
