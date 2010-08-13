package org.resthub.identity.service;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.core.annotation.Auditable;

import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.identity.dao.GroupDao;
import org.resthub.identity.model.Group;
import org.springframework.util.Assert;

@Named("groupService")
public class GroupServiceImpl extends GenericResourceServiceImpl<Group, GroupDao> implements GroupService {

	@Inject
	@Named("groupDao")
	public void setResourceDao(GroupDao groupDao) {
		super.setDao(groupDao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	public Group findByName(String name) {
		Assert.notNull(name, "Group name can't be null");
		List<Group> result = this.dao.findEquals("name", name);

		if( result.size() == 1 ) {
			return result.get(0);
		} else {
			return null;
		}
	}

}
