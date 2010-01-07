package org.resthub.web.controller;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceService;

import com.sun.jersey.api.view.ImplicitProduces;
import com.sun.jersey.api.view.Viewable;

@Singleton
@ImplicitProduces("text/html;qs=5")
public abstract class GenericResourceController<T extends Resource> {
	
	protected Class<T> entityClass;
    private T[] entityClassArray; 
    
	protected ResourceService<T> resourceService;
	
	@Context
	private UriInfo uriInfo;

	
	@SuppressWarnings("unchecked")
	public GenericResourceController() {
		Class clazz = getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();
        while (!(genericSuperclass instanceof ParameterizedType)) {
            clazz = clazz.getSuperclass();
            genericSuperclass = clazz.getGenericSuperclass();
        }
        this.entityClass = (Class<T>) ((ParameterizedType) genericSuperclass)
                .getActualTypeArguments()[0];
            
        entityClassArray = (T[]) Array.newInstance(this.entityClass, 0); 
	}
	
	public void setResourceService(ResourceService<T> resourceService) {
		this.resourceService = resourceService;
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response create(T resource) {
		Resource r = this.resourceService.create(resource);
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		URI uri = uriBuilder.path(r.getName()).build();

		return Response.created(uri).entity(r).build();
	}
	
	@PUT
	@Path("/{name}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response update(@PathParam("name")String name,T resource) { 
		Resource r = this.resourceService.findByName(name);
		resource.setId(r.getId());
		this.resourceService.update(resource);
		return Response.created(uriInfo.getAbsolutePath()).build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getResources() {
		List<T> resources = this.resourceService.findAll();
		return Response.ok(resources.toArray(entityClassArray)).build();
	}
	
	@GET
	@Path("/{name}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getResource(@PathParam("name")String name) {
		T resource =  this.resourceService.findByName(name);
		
		if(resource == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
				
		return Response.ok(resource).build();

	}
	
	@DELETE
	@Path("/{name}")
	public void delete(@PathParam("name")String name) {
		this.resourceService.delete(name);
	}
	
	@GET
	@Path("/{name}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable getResourceView(@PathParam("name")String name) {
		T resource =  this.resourceService.findByName(name);
		return new Viewable("default", resource);
    }
	
	@GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable getResourcesView() {
		return new Viewable("default", new Resource("All"));
    }

}
