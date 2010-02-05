package org.resthub.identity.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

import org.resthub.core.domain.model.User;
import org.resthub.core.service.ResourceService;
import org.resthub.web.controller.GenericResourceController;

@Path("/users")
@Named("userController")
public class UserController extends GenericResourceController<User> {
	
	@Inject
    @Named("userService")
    @Override
	public void setResourceService(ResourceService<User> userService) {
		this.resourceService = userService;
	}
	
}
