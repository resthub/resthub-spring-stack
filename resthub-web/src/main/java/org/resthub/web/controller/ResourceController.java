package org.resthub.web.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/resources")
@Component("resourceController")
@Scope("singleton")
public class ResourceController {
	
	protected ResourceService<Resource> resourceService;
	
	@Inject
    @Named("resourceService")
	public void setResourceService(ResourceService<Resource> resourceService) {
		this.resourceService = resourceService;
	}
	
	@PUT
	@Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public void create(Resource resource) {
		this.resourceService.create(resource);
	}
	
	
	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public List<Resource> findAll() {
		return this.resourceService.findAll();
	}
	

	@GET
	@Path("/{name}")
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public List<Resource> findByName(@PathParam("name")String name) {
		return this.resourceService.findAll();
	}

}
