package org.resthub.identity.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.identity.dao.GroupDao;
import org.resthub.identity.model.Group;

@Named("groupService")
public class GroupServiceImpl extends GenericResourceServiceImpl<Group, GroupDao> implements GroupService {

	@Inject
	@Named("groupDao")
	public void setResourceDao(GroupDao groupDao) {
		super.setDao(groupDao);
	}

}
