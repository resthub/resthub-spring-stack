package org.resthub.web.controller;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.resthub.core.service.GenericService;
import org.resthub.core.util.ClassUtils;
import org.resthub.core.util.MetamodelUtils;
import org.resthub.web.response.PageResponse;
import org.springframework.util.Assert;
import org.synyx.hades.domain.PageRequest;

import com.sun.jersey.api.NotFoundException;

@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
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

    /*
     * (non-Javadoc)
     * 
     * @see org.resthub.web.controller.GenericController#create(T)
     */
    @Override
    @POST
    public T create(T entity) {
        return this.service.create(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.resthub.web.controller.GenericController#update(ID, T)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @PUT
    @Path("/{id}")
    public T update(@PathParam("id") ID id, T entity) {
        Assert.notNull(id, "id cannot be null");
        T retreivedEntity = this.service.findById(id);
        if (retreivedEntity == null) {
            throw new NotFoundException();
        }
        MetamodelUtils utils = new MetamodelUtils<T, ID>((Class<T>) ClassUtils.getGenericTypeFromBean(this.service),
                em.getMetamodel());
        Serializable entityId = utils.getIdFromEntity(entity);
        if ((entityId != null) && !id.equals(this.getIdFromEntity(retreivedEntity))) {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
        if (null == entityId) {
            utils.setIdForEntity(entity, id);
        }
        return this.service.update(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.resthub.web.controller.GenericController#getEntities()
     */
    @Override
    @GET
    @Path("/all")
    public List<T> findAll() {
        return this.service.findAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.resthub.web.controller.GenericController#getEntities(java.lang.Integer
     * , java.lang.Integer)
     */
    @Override
    @GET
    public PageResponse<T> findAll(@QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size) {
        return new PageResponse<T>(this.service.findAll(new PageRequest(page, size)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.resthub.web.controller.GenericController#getResource(ID)
     */
    @Override
    @GET
    @Path("/{id}")
    public T findById(@PathParam("id") ID id) {
        T entity = this.service.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }

        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.resthub.web.controller.GenericController#delete()
     */
    @Override
    @DELETE
    @Path("/all")
    public void delete() {
        this.service.deleteAllWithCascade();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.resthub.web.controller.GenericController#delete(ID)
     */
    @Override
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") ID id) {
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
