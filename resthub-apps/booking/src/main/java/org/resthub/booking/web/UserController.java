package org.resthub.booking.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import org.resthub.booking.model.User;
import org.resthub.core.service.GenericService;
import org.resthub.web.controller.GenericResourceController;

@Path("/user")
@Named("userController")
public class UserController extends GenericResourceController<User> {
    
    @Inject
    @Named("userService")
    @Override
    public void setService(GenericService<User, Long> service) {
        this.service = service;
    }
}
