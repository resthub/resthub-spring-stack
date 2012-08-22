package org.resthub.web.controller;

import java.io.Serializable;
import java.util.List;

import org.resthub.common.service.CrudService;
import org.resthub.web.exception.BadRequestException;
import org.resthub.web.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Abstract REST controller using a service implementation
 * <p/>
 * <p>You should extend this class when you want to use a 3 layers pattern : Repository, Service and Controller
 * If you don't have a real service (also called business layer), consider using RepositoryBasedRestController</p>
 *
 * @param <T>  Your resource class to manage, maybe an entity or DTO class
 * @param <ID> Resource id type, usually Long or String
 * @param <S>  The service class
 * @see RepositoryBasedRestController
 */
public abstract class ServiceBasedRestController<T, ID extends Serializable, S extends CrudService<T, ID>> implements
        RestController<T, ID> {

    protected S service;

    /**
     * You should override this setter in order to inject your service with @Inject annotation
     *
     * @param service The service to be injected
     */
    public void setService(S service) {
        this.service = service;
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
    public T create(@RequestBody T entity) {
        return this.service.create(entity);
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

        T retreivedEntity = this.service.findById(id);
        if (retreivedEntity == null) {
            throw new NotFoundException();
        }

        return this.service.update(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> findAll() {
        return (List<T>) this.service.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<T> findPaginated(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return this.service.findAll(new PageRequest(page, size));
    }

    /**
     * {@inheritDoc}
     */
    public T findById(@PathVariable ID id) {
        T entity = this.service.findById(id);
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
        this.service.deleteAllWithCascade();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(@PathVariable ID id) {
        this.service.delete(id);
    }
}
