package org.resthub.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.resthub.web.exception.BadRequestException;
import org.resthub.web.exception.NotFoundException;
import org.resthub.web.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Abstract REST controller using a repository implementation
 * 
 * <p>You should extend this class when you want to use a 2 layers pattern : Repository and Controller.
 * This is the default controller implementation to use if you have no service (also called business) layer.
 * You will be able to transform it to a ServiceBasedRestController later easily if needed.</p>
 *
 * @param <T> Your resource class to manage, maybe an entity or DTO class
 * @param <ID> Resource id type, usually Long or String
 * @param <R> The repository class
 * 
 * @see ServiceBasedRestController
 **/
public abstract class RepositoryBasedRestController<T, ID extends Serializable, R extends PagingAndSortingRepository<T, ID>> implements
        RestController<T, ID> {

    protected R repository;
    
    protected Logger logger = LoggerFactory.getLogger(RepositoryBasedRestController.class);

    /**
     * You should override this setter in order to inject your repository with @Inject annotation
     * @param repository The repository to be injected
     */
    public void setRepository(R repository) {
        this.repository = repository;
    }

     /**
     * You should implement this method if order to return the identifier of a resource instance
     * 
     * @param resource The resource from whom we need the identifier
     * @return The resource identifier.
     */
    public abstract ID getIdFromResource(T resource);

    /**
     * {@inheritDoc}
     */
    @Override
    public T create(@RequestBody T resource) {
        return this.repository.save(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T update(@PathVariable ID id, @RequestBody T resource) {
        Assert.notNull(id, "id cannot be null");

        Serializable entityId = this.getIdFromResource(resource);
        if ((entityId == null) || (!id.equals(entityId))) {
            throw new BadRequestException();
        }

        T retreivedEntity = this.repository.findOne(id);
        if (retreivedEntity == null) {
            throw new NotFoundException();
        }

        return this.repository.save(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList findAll() {
        return (ArrayList) this.repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResponse findAll(@PathVariable Integer page) {
        logger.error("totototto");
        return new PageResponse(this.repository.findAll(new PageRequest(page, 10)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResponse findAll(@PathVariable Integer page, @PathVariable Integer size) {
        return new PageResponse(this.repository.findAll(new PageRequest(page, size)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T findById(@PathVariable ID id) {
        T entity = this.repository.findOne(id);
        if (entity == null) {
            throw new NotFoundException();
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete() {
        Iterable<T> list = repository.findAll();
        for (T entity : list) {
            repository.delete(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(@PathVariable ID id) {
        this.repository.delete(id);
    }
}
