package org.resthub.identity.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.identity.dao.AbstractPermissionsOwnerDao;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.service.RoleService.RoleChange;
import org.resthub.identity.tools.PermissionsOwnerTools;
import org.springframework.transaction.annotation.Propagation;
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
    protected GroupService groupService;
    @Inject
    @Named("roleService")
    protected RoleService roleService;
    @Inject
    @Named("abstractPermissionsOwnerDao")
    protected AbstractPermissionsOwnerDao abstractPermissionsOwnerDao;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = false)
    public void addPermissionToUser(String userLogin, String permission) {
        if (userLogin != null && permission != null) {
            User u = this.findByLogin(userLogin);
            if (u != null) {
                boolean alreadyAllowed = u.getPermissions().contains(permission);
                if (!alreadyAllowed) {
                    u.getPermissions().add(permission);
                }
                this.update(u);
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
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = false)
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
    public List<User> findAllUsersWithRoles(List<String> roles) {
        List<User> usersWithRole = new ArrayList<User>(); // this list will hold all the users for the result

        // Start by finding the entities directly linked to the roles
        List<AbstractPermissionsOwner> withRoles = abstractPermissionsOwnerDao.getWithRoles(roles);

        // The query may have brought a mix of users and groups,
        // this loop will process them individually to form the final result.
        for (AbstractPermissionsOwner owner : withRoles) {
            this.getUsersFromRootElement(usersWithRole, owner);
        }

        return usersWithRole;
    }

    /**
     * Recursive method to get all the users in an AbstractPermissionsOwner,
     * if the owner is a user, it will be directly added to the list,
     * if the owner is a group, his subgroups will be explored to find users.
     * @param users User list to add users into, must not be null.
     * @param owner Root element to begin exploration.
     */
    private void getUsersFromRootElement(List<User> users, AbstractPermissionsOwner owner) {
        // Stop the processing if one of the parameters is null
        if (users != null && owner != null) {
            // The root element may be user or a group
            if (owner instanceof User) {
                User user = (User) owner;
                // If we have a user, we can't go further so add it if needed and finish.
                if (!users.contains(user)) {
                    users.add(user);
                }
            } else if (owner instanceof Group) {
                // If we have a group, we must get both users and groups having this group as parent
                List<AbstractPermissionsOwner> withGroupAsParent = abstractPermissionsOwnerDao.getWithGroupAsParent((Group) owner);

                // Each result will be recursively evaluated using this method.
                for (AbstractPermissionsOwner child : withGroupAsParent) {
                    this.getUsersFromRootElement(users, child);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void addRoleToUser(String userLogin, String roleName) {
        User u = this.findByLogin(userLogin);
        if (u != null) {
            Role r = roleService.findByName(roleName);
            if (r != null) {
                if (!u.getRoles().contains(r)) {
                    u.getRoles().add(r);
                    this.dao.save(u);
                    this.publishChange(RoleChange.ROLE_ADDED_TO_USER.name(), r, u);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void removeRoleFromUser(String userLogin, String roleName) {
        User u = this.findByLogin(userLogin);
        if (u != null) {
            Role r = roleService.findByName(roleName);
            if (r != null) {
                if (u.getRoles().contains(r)) {
                    u.getRoles().remove(r);
                    this.dao.save(u);
                    this.publishChange(RoleChange.ROLE_REMOVED_FROM_USER.name(), r, u);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Role> getAllUserRoles(String userLogin) {
        List<Role> roles = new ArrayList<Role>();
        User u = this.findByLogin(userLogin);
        if (u != null) {
            this.getRolesFromRootElement(roles, u);
        }

        return roles;
    }

    /**
     * From a root element, take all the roles and go up the hierarchy to find inheritances.
     * @param roles A list of roles that will be filled.
     * @param owner The root element to begin with.
     */
    private void getRolesFromRootElement(List<Role> roles, AbstractPermissionsOwner owner) {
        // Stop the processing if one of the parameters is null
        if (roles != null && owner != null) {
            // Add the roles we find on our path if needed.
            for (Role r : owner.getRoles()) {
                if (!roles.contains(r)) {
                    roles.add(r);
                }
            }

            // Climbing up the hierarchy of groups recursively.
            for (Group g : owner.getGroups()) {
                this.getRolesFromRootElement(roles, g);
            }
        }
    }
}
