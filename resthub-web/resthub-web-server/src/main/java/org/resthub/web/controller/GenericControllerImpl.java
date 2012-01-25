package org.resthub.web.controller;

import java.io.Serializable;
import java.util.List;

import org.resthub.core.service.GenericService;
import org.resthub.web.exception.BadRequestException;
import org.resthub.web.exception.NotFoundException;
import org.resthub.web.response.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class GenericControllerImpl<T, ID extends Serializable, S extends GenericService<T, ID>> implements
        GenericController<T, ID> {

    protected S service;

    public void setService(S service) {
        this.service = service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public T create(@RequestBody T entity) {
        return this.service.create(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public T update(@PathVariable("id") ID id, @RequestBody T entity) {
        Assert.notNull(id, "id cannot be null");
        T retreivedEntity = this.service.findById(id);

        if (retreivedEntity == null) {
            throw new NotFoundException();
        }

        Serializable entityId = this.service.getIdFromEntity(entity);
        if ((entityId == null) || (!id.equals(entityId))) {
            throw new BadRequestException();
        }

        return this.service.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    public List<T> findAll() {
        return this.service.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public PageResponse<T> findAll(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 5 : size;

        return new PageResponse<T>(this.service.findAll(new PageRequest(page, size)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public T findById(@PathVariable("id") ID id) {
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
    @RequestMapping(value = "all", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        this.service.deleteAllWithCascade();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") ID id) {
        this.service.delete(id);
    }
}
