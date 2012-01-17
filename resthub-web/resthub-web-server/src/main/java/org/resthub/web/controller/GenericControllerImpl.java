package org.resthub.web.controller;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.resthub.core.service.GenericService;
import org.resthub.core.util.ClassUtils;
import org.resthub.core.util.MetamodelUtils;
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

    private EntityManager em;
    
    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public GenericControllerImpl() {

    }

    public void setService(S service) {
        this.service = service;
    }

    public S getService() {
        return this.service;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST) @ResponseStatus(HttpStatus.CREATED) @ResponseBody
    public T create(@RequestBody T entity) {
        return this.service.create(entity);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.PUT) @ResponseStatus(HttpStatus.OK)
    public T update(@PathVariable( "id" ) ID id, @RequestBody T entity) {
        Assert.notNull(id, "id cannot be null");
        T retreivedEntity = this.service.findById(id);
        
        if( retreivedEntity == null ){
			throw new NotFoundException();
		}
        
        MetamodelUtils utils = new MetamodelUtils<T, ID>((Class<T>) ClassUtils.getGenericTypeFromBean(this.service),
                em.getMetamodel());
        Serializable entityId = utils.getIdFromEntity(entity);
        
        if ((entityId != null) && !id.equals(this.getIdFromEntity(retreivedEntity))) {
            throw new BadRequestException();
        }
        if (null == entityId) {
            utils.setIdForEntity(entity, id);
        }
        return this.service.update(entity);
    }

    @Override
    @RequestMapping(value = "all", method = RequestMethod.GET) @ResponseBody
    public List<T> findAll() {
        return this.service.findAll();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET) @ResponseBody
    public PageResponse<T> findAll(@RequestParam( "page" ) Integer page, @RequestParam( "size" ) Integer size) {
    	
    	page = (page == null) ? 0 : page;
    	size = (size == null) ? 5 : size;
    	
        return new PageResponse<T>(this.service.findAll(new PageRequest(page, size)));
    }

    @Override
    @RequestMapping(value = "{id}",method = RequestMethod.GET ) @ResponseBody
    public T findById(@PathVariable("id") ID id) {
        T entity = this.service.findById(id);
        if (entity == null) {
        	throw new NotFoundException();
        }

        return entity;
    }

    @Override
    @RequestMapping(value = "all", method = RequestMethod.DELETE) @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        this.service.deleteAllWithCascade();
    }

    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") ID id) {
        this.service.delete(id);
    }

    /**
     * Automatically retrieve ID from entity instance.
     * 
     * @param obj
     *            The object from whom we need primary key
     * @return The corresponding primary key.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ID getIdFromEntity(T obj) {
        MetamodelUtils utils = new MetamodelUtils<T, ID>((Class<T>) ClassUtils.getGenericTypeFromBean(this.service),
                em.getMetamodel());
        return (ID) utils.getIdFromEntity(obj);
    }

}
