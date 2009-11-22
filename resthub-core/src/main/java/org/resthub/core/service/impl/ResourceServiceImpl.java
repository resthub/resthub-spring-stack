package org.resthub.core.service.impl;

import java.util.List;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.service.ResourceService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ResourceServiceImpl<T,D extends ResourceDao<T>> implements ResourceService<T> {
	
	protected D resourceDao;

	public void setResourceDao(D resourceDao) {
		this.resourceDao = resourceDao;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void create(T resource) {
		resourceDao.persist(resource);
	}

	public List<T> findAll() {
		return resourceDao.findAll();
	}

	public T findByPath(String path) {
		return resourceDao.findByPath(path);
	}
	
	public T findByName(String name) {
		return resourceDao.findByName(name);
	}
	
}
