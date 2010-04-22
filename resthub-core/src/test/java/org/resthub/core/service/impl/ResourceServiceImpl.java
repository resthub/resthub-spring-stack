package org.resthub.core.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceService;

@Named("resourceService")
public class ResourceServiceImpl extends
		AbstractResourceGenericServiceImpl<Resource, ResourceDao> implements
		ResourceService {

	@Inject
	@Named("resourceDao")
	@Override
	public void setResourceDao(ResourceDao resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}

}
