package org.resthub.core.service.impl;

import java.io.Serializable;
import java.util.List;

import org.resthub.core.annotation.Auditable;
import org.resthub.core.dao.GenericDao;
import org.resthub.core.service.GenericService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Generic Service implementation.
 * 
 * @param <T> Domain model class managed, must be an Entity
 * @param <D> Generic DAO class
 */
@Transactional(readOnly = true)
public abstract class GenericServiceImpl<T, D extends GenericDao<T, PK>, PK extends Serializable>
		implements GenericService<T, PK> {

	protected D resourceDao;

	public void setResourceDao(D resourceDao) {
		this.resourceDao = resourceDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	@Transactional(readOnly = false)
	public T create(T resource) {
		Assert.notNull(resource, "Resource can't be null");

		return resourceDao.save(resource);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	@Transactional(readOnly = false)
	public T update(T resource) {
		Assert.notNull(resource, "Resource can't be null");

		return resourceDao.save(resource);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	@Transactional(readOnly = false)
	public void delete(T resource) {
		Assert.notNull(resource, "Resource can't be null");

		resourceDao.delete(resource);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	@Transactional(readOnly = false)
	public void delete(PK id) {
		Assert.notNull(id, "Resource ID can't be null");
		resourceDao.delete(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	public T findById(PK id) {
		Assert.notNull(id, "Resource ID can't be null");
		return resourceDao.readByPrimaryKey(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	public List<T> findAll(Integer offset, Integer limit) {
		Integer o = (offset == null || offset < 0) ? 0 : offset;
		Integer l = (limit == null || limit < 0) ? 100 : limit;
		return resourceDao.readAll(o, l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	public Long count() {
		return resourceDao.count();
	}
}
