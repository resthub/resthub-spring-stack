package org.resthub.core.service.impl;

import java.util.List;

import org.resthub.core.domain.dao.AbstractResourceGenericDao;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceGenericService;

public abstract class AbstractResourceGenericServiceImpl<T extends Resource, D extends AbstractResourceGenericDao<T>> extends
		AbstractGenericServiceImpl<T, D, Long> implements
		ResourceGenericService<T> {

	public List<T> findByRef(String ref) {
		return this.resourceDao.findByRef(ref);
	}

	public void setResourceDao(D resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}
	
}
