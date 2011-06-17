package org.resthub.booking.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.resthub.booking.model.User;
import org.resthub.booking.service.UserService;
import org.resthub.web.controller.GenericControllerImpl;

/**
 * @author Guillaume Zurbach
 */
@Path("/user")
@Named("userController")
public class UserController extends GenericControllerImpl<User, Long, UserService> {

    @Inject
    @Named("userService")
    @Override
    public void setService(UserService service) {
        this.service = service;
    }
    
    @GET
    @Path("/username/{username}")
    public User findByUsername(@PathParam("username")String username) {
        return service.findByUsername(username);
    }

}
