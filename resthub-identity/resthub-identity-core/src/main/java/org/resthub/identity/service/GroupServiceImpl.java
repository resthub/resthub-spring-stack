package org.resthub.identity.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.exception.AlreadyExistingEntityException;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.repository.GroupRepository;
import org.resthub.identity.repository.UserRepository;
import org.resthub.identity.service.RoleService.RoleChange;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * An implementation of a Group Service<br/>
 * It's based on both {@link GenericResourceServiceImpl} and {@link GroupService}
 * 
 * It's a bean whose name is "groupService"
 * */
@Named("groupService")
public class GroupServiceImpl extends AbstractTraceableServiceImpl<Group, GroupRepository> implements GroupService {

	/**
	 * The userRepository<br/>
	 * This class need it in order to be able to deal with users
	 */
	protected UserRepository userRepository;
    
	protected RoleService roleService;

	@Inject
	@Named("groupRepository")
	@Override
	public void setRepository(GroupRepository groupRepository) {
		super.setRepository(groupRepository);
	}
	
	@Inject
	@Named("userRepository")
	protected void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Inject
	@Named("roleService")
	protected void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Group findByName(String name) {
		Assert.notNull(name, "Group name can't be null");
		List<Group> result = this.repository.findByName(name);
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
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
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
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
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
	public Group create(Group group) throws AlreadyExistingEntityException {
		Group existingGroup = this.findByName(group.getName());
		if (existingGroup == null) {
			// Overriden method call.
			group = super.create(group);
			// Publish notification
			publishChange(GroupServiceChange.GROUP_CREATION.name(), group);
			return group;
		} else {
			throw new AlreadyExistingEntityException("Group " + group.getName() + " already exists.");
		}
	} // create().

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public Group update(Group group) throws AlreadyExistingEntityException {
		// Check if there is an already existing group with this name with a
		// different ID
		Group existingGroup = this.findByName(group.getName());
		if (existingGroup == null || existingGroup.getId() == group.getId()) {
			group = super.update(group);
		} else {
			throw new AlreadyExistingEntityException("Group " + group.getName() + " already exists.");
		}
		return group;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Group group) {
		// Find the users who are in this group
		List<User> users = userRepository.getUsersFromGroup(group.getName());

		// Unlink each user from this group
		for (User user : users) {
			user.getGroups().remove(group);
		}
		userRepository.save(users);

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
					this.repository.save(g);
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
					this.repository.save(g);
					this.publishChange(RoleChange.ROLE_REMOVED_FROM_GROUP.name(), r, g);
				}
			}
		}
	}
}
