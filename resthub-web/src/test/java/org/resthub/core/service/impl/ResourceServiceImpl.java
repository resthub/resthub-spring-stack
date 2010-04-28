package org.resthub.core.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.dao.GenericResourceDao;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceService;

@Named("resourceService")
public class ResourceServiceImpl extends
		GenericResourceServiceImpl<Resource, GenericResourceDao<Resource>> implements
		ResourceService {

	@Inject
	@Named("resourceDao")
	@Override
	public void setResourceDao(GenericResourceDao resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}

}
