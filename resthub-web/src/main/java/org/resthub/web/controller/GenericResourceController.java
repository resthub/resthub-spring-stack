package org.resthub.web.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.resthub.core.domain.model.Resource;

public interface GenericResourceController <T extends Resource> {
	
	@POST
	@Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	T create(T resource);
	
	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	T[] findAll();
	
	@GET
	@Path("/{name}")
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	T[] findByName(@PathParam("name")String name);

}
