package org.resthub.identity.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import org.resthub.core.service.GenericResourceService;

import org.resthub.identity.model.User;
import org.resthub.web.controller.GenericResourceController;

@Path("/users")
@Named("userController")
public class UserController extends GenericResourceController<User, GenericResourceService<User>> {
	
    @Inject
    @Named("userService")
    @Override
    public void setService(GenericResourceService<User> userService) {
        this.service = userService;
    }
	
}
