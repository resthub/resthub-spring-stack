package org.resthub.web.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.GenericResourceService;

@Path("/resources")
@Named("resourceController")
public class ResourceController extends GenericResourceController<Resource> {
	
	@Inject
    @Named("resourceService")
    @Override
	public void setResourceService(GenericResourceService<Resource> resourceService) {
		this.resourceService = resourceService;
	}
	
}
