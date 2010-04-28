package org.resthub.web.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceService;
import org.resthub.core.service.impl.GenericResourceServiceImpl;
import org.resthub.web.domain.dao.ResourceDao;

@Named("resourceService")
public class ResourceServiceImpl extends
		GenericResourceServiceImpl<Resource, ResourceDao> implements
		ResourceService {

	@Inject
	@Named("resourceDao")
	@Override
	public void setResourceDao(ResourceDao resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}

}
