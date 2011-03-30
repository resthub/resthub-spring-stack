package org.resthub.identity.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.identity.dao.PermissionsOwnerDao;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.resthub.identity.tools.PermissionsOwnerTools;
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
public class UserServiceImpl extends AbstractEncryptedPasswordUserService {

    @Inject
    @Named("userDao")
    public void setResourceDao(UserDao userDao) {
        super.setDao(userDao);
    }
    @Inject
    @Named("groupService")
    GroupService groupService;

    /**
     * Retrieves a user by his login
     *
     * @param login
     *            the login to look for
     * @return the corresponding User object if founded, null otherwise
     * */
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
     * {@inheritDoc}
     */
    @Override
    public List<String> getUserPermissions(String login) {
        List<String> p = null;
        User u = this.findByLogin(login);
        if (u != null) {
            p = PermissionsOwnerTools.getInheritedPermission(u);
        }
        return p;
    }

    /**
     * gets the User's direct Permissions
     *
     * @param login
     *            the login of the user
     * @return permissions of the user.
     */
    public List<String> getUserDirectPermissions(String login) {
        List<String> p = null;
        User u = this.findByLogin(login);
        if (u != null) {
            p = u.getPermissions();
        }
        return p;
    }

    /**
     * Add a permission to an user
     *
     * @param userLogin
     *            the login of the user
     * @param permission
     *            the permission to be added
     */
    @Transactional
    public void addPermissionToUser(String userLogin, String permission) {
        if (userLogin != null && permission != null) {
            User u = this.findByLogin(userLogin);
            if (u != null) {
                boolean alreadyAllowed = u.getPermissions().contains(permission);
                if (!alreadyAllowed) {
                    u.getPermissions().add(permission);
                }
            }
        }
    }

    /**
     * Remove the permission for the given user
     *
     * @param userLogin
     *            the login of the user
     * @param permission
     *            the permission to delete
     */
    @Transactional
    public void removePermissionFromUser(String userLogin, String permission) {
        if (userLogin != null && permission != null) {
            User u = this.findByLogin(userLogin);
            if (u != null) {
                while (u.getPermissions().contains(permission)) {
                    u.getPermissions().remove(permission);
                }
            }
        }
    }

    /**
     * Add a group from one user's groups
     *
     * @param userLogin
     *            the login of the user to whom to group should be added
     * @param groupeName
     *            the name of the group to add from the user's group list
     */
    @Transactional
    public void addGroupToUser(String userLogin, String groupName) {
        if (userLogin != null && groupName != null) {
            User u = this.findByLogin(userLogin);
            Group g = groupService.findByName(groupName);
            if (u != null && g != null) {
                u.getGroups().add(g);
            }
        }
    }

    /**
     * Remove a group from one user's groups
     *
     * @param userLogin
     *            the login of the user to whom to group should be remove
     * @param groupeName
     *            the name of the group to remove from the user's group list
     */
    @Transactional
    public void removeGroupFromUser(String userLogin, String groupName) {
        if (userLogin != null && groupName != null) {
            User u = this.findByLogin(userLogin);
            Group g = groupService.findByName(groupName);
            if (u != null && g != null) {
                u.getGroups().remove(g);
            }
        }
    }

    @Override
    public List<User> getUsersFromGroup(String groupName) {
        List<User> usersFromGroup = this.dao.getUsersFromGroup(groupName);
        return usersFromGroup;
    }
}
