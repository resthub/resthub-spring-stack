package org.resthub.identity.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.service.impl.ResourceServiceImpl;
import org.resthub.identity.domain.model.Group;
import org.resthub.identity.service.GroupService;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends ResourceServiceImpl<Group, ResourceDao<Group>> implements GroupService {
	
	@Override
	@Inject
	@Named("groupDao")
	public void setResourceDao(ResourceDao<Group> resourceDao) {
		super.setResourceDao(resourceDao);
	}
}
