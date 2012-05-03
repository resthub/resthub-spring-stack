package org.resthub.web.controller;

import java.io.Serializable;
import java.util.List;

import org.resthub.web.exception.BadRequestException;
import org.resthub.web.exception.NotFoundException;
import org.resthub.web.response.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class GenericControllerImpl<T, ID extends Serializable, R extends PagingAndSortingRepository<T, ID>> implements
        GenericController<T, ID> {

    protected R repository;

    public void setRepository(R repository) {
        this.repository = repository;
    }

    /**
     * Retrieve ID from entity instance.
     * 
     * @param resource
     *            the resource from whom we need primary key
     * @return The corresponding primary key.
     */
    public abstract ID getIdFromEntity(T resource);
    
    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public T create(@RequestBody T entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public T update(@PathVariable("id") ID id, @RequestBody T entity) {
        Assert.notNull(id, "id cannot be null");
        
        Serializable entityId = this.getIdFromEntity(entity);
        if ((entityId == null) || (!id.equals(entityId))) {
            throw new BadRequestException();
        }
        
        T retreivedEntity = this.repository.findOne(id);
        if (retreivedEntity == null) {
            throw new NotFoundException();
        }

        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    public List<T> findAll() {
        return  (List<T>) this.repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public PageResponse<T> findAll(@RequestParam(value="page", required=false) Integer page, @RequestParam(value="size", required=false) Integer size) {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 5 : size;

        return new PageResponse<T>(this.repository.findAll(new PageRequest(page, size)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public T findById(@PathVariable("id") ID id) {
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
    @RequestMapping(value="all", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") ID id) {
        this.repository.delete(id);
    }
}
