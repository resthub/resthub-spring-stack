package org.resthub.core.service;

import java.io.Serializable;
import java.util.List;

import org.resthub.core.audit.annotation.Auditable;
import org.resthub.core.dao.GenericDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;

/**
 * Generic Service implementation.
 * 
 * @param <T> Domain model class managed, must be an Entity
 * @param <D> Generic DAO class
 * 
 */
@Transactional(readOnly = true)
public abstract class GenericServiceImpl<T, D extends GenericDao<T, ID>, ID extends Serializable>
		implements GenericService<T, ID> {

	protected D dao;

	public void setDao(D resourceDao) {
		this.dao = resourceDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	@Transactional(readOnly = false)
	public T create(T resource) {
		Assert.notNull(resource, "Resource can't be null");

		return dao.save(resource);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	@Transactional(readOnly = false)
	public T update(T resource) {
		Assert.notNull(resource, "Resource can't be null");

		return dao.save(resource);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	@Transactional(readOnly = false)
	public void delete(T resource) {
		Assert.notNull(resource, "Resource can't be null");

		dao.delete(resource);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	@Transactional(readOnly = false)
	public void delete(ID id) {
		Assert.notNull(id, "Resource ID can't be null");
		dao.delete(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	public T findById(ID id) {
		Assert.notNull(id, "Resource ID can't be null");
		return dao.readByPrimaryKey(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	public List<T> findAll(Integer offset, Integer limit) {
		Integer o = (offset == null || offset < 0) ? 0 : offset;
		Integer l = (limit == null || limit < 0) ? 100 : limit;
		return dao.readAll(o, l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	public List<T> findAll() {
		return dao.readAll();
	}

    /**
	 * {@inheritDoc}
	 */
	@Override
	public Page<T> findAll(Pageable pageRequest) {
		return dao.readAll(pageRequest);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Auditable
	public Long count() {
		return dao.count();
	}
}
