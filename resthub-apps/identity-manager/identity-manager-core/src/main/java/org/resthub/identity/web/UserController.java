package org.resthub.identity.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

import org.resthub.core.service.GenericService;
import org.resthub.identity.model.User;
import org.resthub.web.controller.GenericResourceController;

@Path("/users")
@Named("userController")
public class UserController extends GenericResourceController<User> {
	
    @Inject
    @Named("userService")
    @Override
    public void setService(GenericService<User, Long> userService) {
        this.service = userService;
    }
	
}
