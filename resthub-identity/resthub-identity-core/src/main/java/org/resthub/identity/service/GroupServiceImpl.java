package org.resthub.identity.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.identity.dao.PermissionsOwnerDao;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.service.RoleService.RoleChange;
import org.resthub.identity.service.tracability.ServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * An implementation of a Group Service<br/>
 * It's based on both {@link GenericResourceServiceImpl} and
 * {@link GroupService}
 * 
 * It's a bean whose name is "groupService"
 * */
@Named("groupService")
public class GroupServiceImpl extends GenericResourceServiceImpl<Group, PermissionsOwnerDao<Group>> implements
        GroupService {

    /**
     * Class logger
     */
    final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    /**
     * Set of registered listeners
     */
    protected Set<ServiceListener> listeners = new HashSet<ServiceListener>();
    /**
     * The userDao<br/>
     * This class need it in order to be able to deal with users
     */
    @Inject
    @Named("userDao")
    protected UserDao userDao;
    protected RoleService roleService;

    @Inject
    @Named("roleService")
    protected void setRoleDao(RoleService roleService) {
        this.roleService = roleService;
    }

    @Inject
    @Named("groupDao")
    public void setResourceDao(PermissionsOwnerDao<Group> groupDao) {
        super.setDao(groupDao);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group findByName(String name) {
        Assert.notNull(name, "Group name can't be null");
        List<Group> result = this.dao.findEquals("name", name);
        int size = result.size();
        Group g = null;
        if (size == 1) {
            g = result.get(0);
        }
        return g;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> findAllGroups() {
        return this.dao.readAll();
    }

    /**
     * add a group to a group
     *
     * @param groupName
     *            the name of the group to whom the subGroup should be added
     * @param subGroupName
     *            the name of the group which should be added
     *
     */
    @Override
    @Transactional
    public void addGroupToGroup(String groupName, String subGroupName) {
        if (groupName != null && subGroupName != null) {
            Group group = this.findByName(groupName);
            Group subGroup = this.findByName(subGroupName);
            if (group != null && subGroup != null) {
                boolean contain = group.getGroups().contains(subGroup);
                if (!contain) {
                    group.getGroups().add(subGroup);
                    // Publish notification
                    publishChange(GroupServiceChange.GROUP_ADDED_TO_GROUP.name(), subGroup, group);
                }
            }
        }
    }

    /**
     * add a permission to a group
     *
     * @param groupName
     *            the name of the group
     * @param permission
     *            the permission to be added
     */
    @Override
    @Transactional
    public void addPermissionToGroup(String groupName, String permission) {
        if (groupName != null && permission != null) {
            Group g = this.findByName(groupName);
            if (g != null) {
                boolean contains = g.getPermissions().contains(permission);
                if (!contains) {
                    g.getPermissions().add(permission);
                }
            }
        }
    }

    /**
     * Get the groups direct Permissions
     *
     * @param the
     *            name of the group to whom the permisisons are requested
     *
     * @return the list of permissions that the group has directly (non
     *         Inherited)
     * */
    @Override
    @Transactional
    public List<String> getGroupDirectPermissions(String groupName) {
        List<String> permissions = null;
        if (groupName != null) {
            Group g = this.findByName(groupName);
            if (g != null) {
                permissions = g.getPermissions();
            }
        }
        return permissions;
    }

    /**
     * remove a group from a group
     *
     * @param groupName
     *            the name of the group to which the subGroup should be remove
     *
     * @param subGroupName
     *            the name of the subGroup which should be removed from the
     *            other group list
     *
     */
    @Override
    public void removeGroupFromGroup(String groupName, String subGroupName) {
        if (groupName != null && subGroupName != null) {
            Group group = this.findByName(groupName);
            Group subGroup = this.findByName(subGroupName);
            if (group != null && subGroup != null) {
                boolean contain = group.getGroups().contains(subGroup);
                if (contain) {
                    group.getGroups().remove(subGroup);
                    // Publish notification
                    publishChange(GroupServiceChange.GROUP_REMOVED_FROM_GROUP.name(), subGroup, group);
                }
            }
        }
    }

    /**
     * Remove a permission from a group
     *
     * @param groupName
     *            the name of the group from which the permission should be
     *            removed
     *
     * @param permission
     *            the permission which should be removed
     * */
    @Override
    @Transactional
    public void removePermissionFromGroup(String groupName, String permission) {
        Group g = this.findByName(groupName);
        if (g != null && permission != null) {
            List<String> permissions = g.getPermissions();
            while (permissions.contains(permission)) {
                permissions.remove(permission);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Long id) {
        // Get the actual group
        Group group = this.findById(id);

        // Let the other delete method do the job
        this.delete(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public Group create(Group created) {
        // Overriden method call.
        created = super.create(created);
        // Publish notification
        publishChange(GroupServiceChange.GROUP_CREATION.name(), created);
        return created;
    } // create().

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Group group) {
        // Find the users who are in this group
        List<User> users = userDao.getUsersFromGroup(group.getName());

        // Unlink each user from this group
        for (User user : users) {
            user.getGroups().remove(group);
        }
        userDao.save(users);

        // Proceed with the actual delete
        super.delete(group);

        // Publish notification
        publishChange(GroupServiceChange.GROUP_DELETION.name(), group);
    } // delete().

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void addRoleToGroup(String groupName, String roleName) {
        Group g = this.findByName(groupName);
        if (g != null) {
            Role r = roleService.findByName(roleName);
            if (r != null) {
                if (!g.getRoles().contains(r)) {
                    g.getRoles().add(r);
                    this.dao.save(g);
                    this.publishChange(RoleChange.ROLE_ADDED_TO_GROUP.name(), r, g);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void removeRoleFromGroup(String groupName, String roleName) {
        Group g = this.findByName(groupName);
        if (g != null) {
            Role r = roleService.findByName(roleName);
            if (r != null) {
                if (g.getRoles().contains(r)) {
                    g.getRoles().remove(r);
                    this.dao.save(g);
                    this.publishChange(RoleChange.ROLE_REMOVED_FROM_GROUP.name(), r, g);
                }
            }
        }
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
                logger.warn("[publishChange] Cannot publish " + type + " changes", exc);
            }
        }
    } // publishChange().
}
