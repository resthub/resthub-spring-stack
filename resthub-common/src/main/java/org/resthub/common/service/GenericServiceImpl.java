package org.resthub.common.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.util.Assert;

/**
 * Generic Service implementation.
 * 
 * @param <T>
 *            Domain model class managed, must be an Entity
 * @param <D>
 *            Repository class
 * 
 */
public abstract class GenericServiceImpl<T, ID extends Serializable, D extends PagingAndSortingRepository<T, ID>>
        implements GenericService<T, ID> {

    protected D repository;

    /**
     * @param repository
     *            the repository to set
     */
    public void setRepository(D repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T create(T resource) {
        Assert.notNull(resource, "Resource can't be null");
        return repository.save(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T update(T resource) {
        Assert.notNull(resource, "Resource can't be null");
        return repository.save(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(T resource) {
        Assert.notNull(resource, "Resource can't be null");
        repository.delete(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(ID id) {
        Assert.notNull(id, "Resource ID can't be null");
        repository.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllWithCascade() {
        Iterable<T> list = repository.findAll();
        for (T entity : list) {
            repository.delete(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T findById(ID id) {
        Assert.notNull(id, "Resource ID can't be null");
        return repository.findOne(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> findAll() {
        return (List<T>) repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<T> findAll(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long count() {
        return repository.count();
    }
}
