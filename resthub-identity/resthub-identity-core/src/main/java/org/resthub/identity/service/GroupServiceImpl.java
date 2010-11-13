package org.resthub.identity.service;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.identity.dao.GroupDao;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Named("groupService")
public class GroupServiceImpl extends GenericResourceServiceImpl<Group, GroupDao> implements GroupService {

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
	 * {@inheritDoc}
	 */
	@Override
	public Group findByName(String name) {
		Assert.notNull(name, "Group name can't be null");
		List<Group> result = this.dao.findEquals("name", name);

		if( result.size() == 1 ) {
			return result.get(0);
		} else {
			return null;
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Group> findAllGroups() {
		return this.dao.readAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public void removeUser( Group group, User user ) {
		//group.removeUser( user );
		user.removeGroup( group );
		this.userDao.save( user );
	}
}
