package org.resthub.core.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.annotation.Auditable;
import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Generic Service implementation.
 * @param <T> Resource Model
 * @param <D> Resource DAO
 */
@Transactional(readOnly = true)
public abstract class AbstractResourceServiceImpl<T extends Resource, D extends ResourceDao<T>>
        implements ResourceService<T> {

    protected D resourceDao;

    @Inject
    @Named("resourceDao")
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
        Assert.isNull(resource.getId(), "Creating a already persisted instance " + resource);

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
        Assert.notNull(resource.getId(), "Updating a detached instance that never be persisted " + resource);
        
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
        Assert.notNull(resource.getId(), "Removing a detached instance that never be persisted " + resource);

        resourceDao.delete(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Auditable
    @Transactional(readOnly = false)
    public void delete(Long id) {
        Assert.notNull(id, "Resource ID can't be null");
        resourceDao.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Auditable
    public T findById(Long id) {
        Assert.notNull(id, "Resource ID can't be null");
        return resourceDao.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Auditable
    public List<T> findAll(Integer offset, Integer limit) {
        Integer o = (offset == null || offset < 0) ? 0 : offset;
        Integer l = (limit == null || limit < 0) ? 100 : limit;
        return resourceDao.findAll(o, l);
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
