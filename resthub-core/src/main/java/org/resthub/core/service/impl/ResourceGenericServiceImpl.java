package org.resthub.core.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.dao.ResourceGenericDao;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceGenericService;

public abstract class ResourceGenericServiceImpl<T extends Resource, D extends ResourceGenericDao<T>> extends
		ResthubGenericServiceImpl<T, D, Long> implements
		ResourceGenericService<T> {

	@Inject
	@Named("resourceDao")
	@Override
	public void setResourceDao(D resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}
	
}
