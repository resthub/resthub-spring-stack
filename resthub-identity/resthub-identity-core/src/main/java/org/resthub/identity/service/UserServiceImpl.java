package org.resthub.identity.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.resthub.identity.service.tracability.ServiceListener;
import org.resthub.identity.tools.PermissionsOwnerTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	/**
	 * Class logger
	 */
	final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	/**
	 * Set of registered listeners
	 */
	protected Set<ServiceListener> listeners = new HashSet<ServiceListener>();
	
    @Inject
    @Named("userDao")
    public void setResourceDao(UserDao userDao) {
        super.setDao(userDao);
    }
    @Inject
    @Named("groupService")
    GroupService groupService;

    /**
     * {@inheritDoc}
     */
    @Override
	@Transactional(readOnly=false)
    public User create(User user) {
    	// Overloaded method call
    	User created = super.create(user);
        // Publish notification
        publishChange(UserServiceChange.USER_CREATION.name(), created);
        return created;
    } // create().
    
    /**
     * {@inheritDoc}
     */
    @Override
	@Transactional(readOnly=false)
    public void delete(Long id) {
    	User deleted = findById(id);
    	// Overloaded method call
    	super.delete(id);
        // Publish notification
        publishChange(UserServiceChange.USER_DELETION.name(), deleted);
    } // delete().
    
    /**
     * {@inheritDoc}
     */
    @Override
	@Transactional(readOnly=false)
    public void delete(User user) {
    	super.delete(user);
        // Publish notification
        publishChange(UserServiceChange.USER_DELETION.name(), user);
    } // delete().
    
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
            // Publish notification
            publishChange(UserServiceChange.USER_ADDED_TO_GROUP.name(), u, g);
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
            // Publish notification
            publishChange(UserServiceChange.USER_REMOVED_FROM_GROUP.name(), u, g);
        }
    }

    @Override
    public List<User> getUsersFromGroup(String groupName) {
        List<User> usersFromGroup = this.dao.getUsersFromGroup(groupName);
        return usersFromGroup;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(ServiceListener listener) {
    	// Adds a new listener if needed.
    	if (!listeners.contains(listener)) {
    		listeners.add(listener);
    	}
    } // addListener().
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(ServiceListener listener) {
    	// Adds a new listener if needed.
    	if (listeners.contains(listener)) {
    		listeners.remove(listener);
    	}
    } // removeListener().
    
    /**
     * Sends a notification to every listernes registered.
     * Do not fail if a user thrown an exception (report exception in logs).
     * 
     * @param type Type of notification.
     * @param arguments Notification arguments.
     */
    protected void publishChange(String type, Object... arguments) {
	    for (ServiceListener listener : listeners) {           
	    	try {
	    		// Sends notification to each known listeners
	    		listener.onChange(type, arguments);
	        } catch (Exception exc) {
	        	// Log exception
	        	logger.warn("[publishChange] Cannot bublish " + type + " changes", exc);
	        }
	    }
    } // publishChange().
}
