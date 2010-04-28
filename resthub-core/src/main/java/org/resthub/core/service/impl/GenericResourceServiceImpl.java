package org.resthub.core.service.impl;

import java.util.List;

import org.resthub.core.domain.dao.GenericResourceDao;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.GenericResourceService;

public abstract class GenericResourceServiceImpl<T extends Resource, D extends GenericResourceDao<T>> extends
		GenericServiceImpl<T, D, Long> implements
		GenericResourceService<T> {

	public List<T> findByRef(String ref) {
		return this.resourceDao.findByRef(ref);
	}

	public void setResourceDao(D resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}
	
}
