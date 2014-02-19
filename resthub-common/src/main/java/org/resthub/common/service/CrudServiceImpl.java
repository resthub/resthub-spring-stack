package org.resthub.common.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Set;

/**
 * CRUD service that uses a {@link org.springframework.data.repository.PagingAndSortingRepository} Spring Data repository implementation
 *
 * You should extend it and inject your Repository bean by overriding {@link #setRepository(org.springframework.data.repository.PagingAndSortingRepository)}
 *
 * @param <T> Your resource class to manage, usually an entity class
 * @param <ID> Resource id type, usually Long or String
 * @param <R> The repository class
 */
@Transactional(readOnly = true)
public class CrudServiceImpl<T, ID extends Serializable, R extends PagingAndSortingRepository<T, ID>> implements
        CrudService<T, ID> {

    protected R repository;

    /**
     * @param repository the repository to set
     */
    public void setRepository(R repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public T create(T resource) {
        Assert.notNull(resource, "Resource can't be null");
        return repository.save(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public T update(T resource) {
        Assert.notNull(resource, "Resource can't be null");
        return repository.save(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(T resource) {
        Assert.notNull(resource, "Resource can't be null");
        repository.delete(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(ID id) {
        Assert.notNull(id, "Resource ID can't be null");
        repository.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
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
    public Iterable<T> findByIds(Set<ID> ids) {
        Assert.notNull(ids, "Resource ids can't be null");
        return repository.findAll(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<T> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<T> findAll(Pageable pageRequest) {
        Assert.notNull(pageRequest, "page request can't be null");
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
