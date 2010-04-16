package org.resthub.web.controller;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.resthub.core.AbstractResourceClassAware;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceGenericService;

import com.sun.jersey.api.view.ImplicitProduces;
import com.sun.jersey.api.view.Viewable;

@Singleton
@ImplicitProduces("text/html;qs=5")
public abstract class GenericResourceController<T extends Resource> extends AbstractResourceClassAware<T> {

    protected T[] resourceClassArray;
    protected ResourceGenericService<T> resourceService;
    @Context
    private UriInfo uriInfo;

    @SuppressWarnings("unchecked")
    public GenericResourceController() {
        super();
        resourceClassArray = (T[]) Array.newInstance(this.resourceClass, 0);
    }

    public void setResourceService(ResourceGenericService<T> resourceService) {
        this.resourceService = resourceService;
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(T resource) {
        Resource r = this.resourceService.create(resource);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path(r.getId().toString()).build();

        return Response.created(uri).entity(r).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") Long id, T resource) {
        Resource r = this.resourceService.findById(id);
        resource.setId(r.getId());
        this.resourceService.update(resource);
        return Response.created(uriInfo.getAbsolutePath()).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getResources() {
        List<T> resources = this.resourceService.findAll(null, null);
        return Response.ok(resources.toArray(resourceClassArray)).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getResource(@PathParam("id") Long id) {
        T resource = this.resourceService.findById(id);
        if (resource == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok(resource).build();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.resourceService.delete(id);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable getResourceView(@PathParam("id") Long id) {
        T resource = this.resourceService.findById(id);
        return new Viewable("default", resource);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable getResourcesView() {
        List<T> resources = this.resourceService.findAll(null, null);
        return new Viewable("default", resources.toArray(resourceClassArray));
    }
}
