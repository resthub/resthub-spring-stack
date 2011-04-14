package org.resthub.identity.service;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.identity.dao.AbstractPermissionsOwnerDao;
import org.resthub.identity.dao.RoleDao;
import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
@Named("roleService")
public class RoleServiceImpl extends AbstractTraceableServiceImpl<Role, RoleDao> implements RoleService {

    @Inject
    @Named("abstractPermissionsOwnerDao")
    protected AbstractPermissionsOwnerDao abstractPermissionsOwnerDao;
    @Inject
    @Named("userService")
    protected UserService userService;
    protected GroupService groupService;

    @Inject
    @Named("groupService")
    protected void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * ${@inheritDoc}
     */
    @Override
    @Inject
    @Named("roleDao")
    public void setDao(RoleDao resourceDao) {
        super.setDao(resourceDao);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Long id) {
        // Get the actual group
        Role role = this.findById(id);

        // Let the other delete method do the job
        this.delete(role);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Role role) {
        // Find the elements with this role
        List<AbstractPermissionsOwner> withRole = abstractPermissionsOwnerDao.getWithRoles(Arrays.asList(role.getName()));

        for (AbstractPermissionsOwner owner : withRole) {
            if (owner instanceof Group) {
                this.groupService.removeRoleFromGroup(((Group) owner).getName(), role.getName());
            } else if (owner instanceof User) {
                this.userService.removeRoleFromUser(((User) owner).getLogin(), role.getName());
            }
        }

        // Proceed with the actual delete
        super.delete(role);
        this.publishChange(RoleChange.ROLE_DELETION.name(), role);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role findByName(String name) {
        List<Role> roles = this.dao.findEquals("name", name);
        int size = roles.size();
        return (size > 0) ? roles.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Role> findByNameLike(String pattern) {
        List<Role> roles = this.dao.findLike("name", pattern);
        return roles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public Role create(Role resource) {
        // Call the standard role creation
        Role createdRole = super.create(resource);
        // Publish the creation event
        this.publishChange(RoleChange.ROLE_CREATION.name(), createdRole);
        return createdRole;
    }
}
