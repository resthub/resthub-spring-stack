package org.resthub.identity.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.identity.dao.PermissionsOwnerDao;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
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
     * The userDao<br/>
     * This class need it in order to be able to deal with users
     */
    @Inject
    @Named("userDao")
    UserDao userDao;

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
        Group g = this.findByName(groupName);
        if (g != null && subGroupName != null) {
            List<String> groups = g.getPermissions();
            while (groups.contains(subGroupName)) {
                groups.remove(subGroupName);
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
}
