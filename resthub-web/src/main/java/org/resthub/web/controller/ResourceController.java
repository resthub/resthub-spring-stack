package org.resthub.web.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;

import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceService;

@Path("/resources")
@Named("resourceController")
@Singleton
public class ResourceController extends GenericResourceControllerImpl<Resource> {
	
	@Inject
    @Named("resourceService")
    @Override
	public void setResourceService(ResourceService<Resource> resourceService) {
		this.resourceService = resourceService;
	}
	
}
