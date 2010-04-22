package org.resthub.identity.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

import org.resthub.core.service.ResourceGenericService;
import org.resthub.identity.domain.model.User;
import org.resthub.web.controller.AbstractGenericResourceController;

@Path("/users")
@Named("userController")
public class UserController extends AbstractGenericResourceController<User> {
	
	@Inject
    @Named("userService")
	public void setResourceService(ResourceGenericService<User> userService) {
		this.resourceService = userService;
	}
	
}
