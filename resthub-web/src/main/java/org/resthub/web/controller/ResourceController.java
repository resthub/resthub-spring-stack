package org.resthub.web.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.resthub.core.domain.model.Resource;

@Path("/resources")
public class ResourceController {
	
	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public Resource getResource() {
		return new Resource("TestPersistName");
	}

}
