package org.resthub.identity.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.annotation.Auditable;
import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.identity.dao.GroupDao;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.Group;
import org.springframework.util.Assert;

/**
 * An implementation of a Group Service<br/>
 * It's based on both {@link GenericResourceServiceImpl} and
 * {@link GroupService}
 * 
 * It's a bean whose name is "groupService"
 * */
@Named("groupService")
public class GroupServiceImpl extends
		GenericResourceServiceImpl<Group, GroupDao> implements GroupService {

	/**
	 * The userDao<br/>
	 * This class need it in order to be able to deal with users
	 */
	UserDao userDao;

	@Inject
	@Named("groupDao")
	public void setResourceDao(GroupDao groupDao) {
		super.setDao(groupDao);
	}

	@Inject
	@Named("userDao")
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * A Reference to the userService This is needed for the creation of Groups
	 * including users
	 */
	UserService userService;

	@Inject
	@Named("userService")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
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
	@Auditable
	public List<Group> findAllGroups() {
		return this.dao.readAll();
	}

}
