package org.resthub.identity.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.annotation.Auditable;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * An implementation of a UserService dealing with OAuth2 authentication <br/>
 * It is based on AbstractEncryptedPasswordUserService
 * 
 * It is a bean whose name is userService
 * 
 * */
@Named("userService")
@Transactional
public class UserServiceImpl extends AbstractEncryptedPasswordUserService {

	@Inject
	@Named("userDao")
	public void setResourceDao(UserDao userDao) {
		super.setDao(userDao);
	}

	GroupService groupService;

	@Inject
	@Named("groupService")
	public void setGroupService(GroupService g) {
		groupService = g;
	}

	/**
	 * I just don't know why we need that, but we do if we want to add User and
	 * link User with group automatically
	 */
	@Override
	@Transactional
	public User create(User u) {
		u = super.create(u);
		return u;
	}

	/**
	 * Retrieves a user by his login
	 * 
	 * @param login
	 *            the login to look for
	 * @return the corresponding User object if founded, null otherwise
	 * */
	@Auditable
	public User findByLogin(String login) {
		Assert.notNull(login, "User login can't be null");
		List<User> result = this.dao.findEquals("login", login);
		int size = result.size();
		return (size > 0) ? result.get(0) : null;
	}

	/**
	 * Authenticate a user based on login and Password and returns the login if
	 * successful
	 * 
	 * @param login
	 * @param password
	 * @return login , the login of the user if the authentication succeed, null
	 *         otherwise
	 */
	public String getUser(String login, String password) {
		User u = this.authenticateUser(login, password);
		return (u != null) ? u.getLogin() : null;
	}

	/**
	 * gets the User's Permissions
	 * 
	 * @param login
	 *            the login of the user
	 * @return permissions of the user. In this implementation inherited
	 *         permissions from group to which the user belong are taken into
	 *         accounts
	 */
	public List<String> getUserPermissions(String login) {
		List<String> userPermissisons = new ArrayList<String>();
		User u = this.findByLogin(login);
		List<String> tmpPermissions;
		if (u != null) {
			tmpPermissions = this.findByLogin(login).getPermissions();
			if (tmpPermissions != null) {
				userPermissisons.addAll(tmpPermissions);
			}
		}
		List<Group> lG = this.findByLogin(login).getGroups();
		if (lG != null) {
			for (Group g : lG) {
				tmpPermissions = g.getPermissions();
				if (tmpPermissions != null) {
					userPermissisons.addAll(tmpPermissions);
				}
			}
		}
		return userPermissisons;
	}

	/**
	 * Remove a group from one user's groups
	 * 
	 * @param userLogin
	 *            the login of the user to whom to group should be remove
	 * @param groupeName
	 *            the name of the group to remove from the user's group list
	 */
	public void removeGroupForUser(String userLogin, String groupName) {
		if (userLogin != null && groupName != null) {
			User u = this.findByLogin(userLogin);
			Group g = groupService.findByName(groupName);
			if (u != null && g != null) {
				u.removeFromGroup(g);
			}
		}
	}
}
