package org.resthub.booking.web;

import java.net.URI;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.resthub.booking.model.User;
import org.resthub.booking.service.UserService;
import org.resthub.web.controller.GenericResourceController;

@Path("/user")
@Named("userController")
public class UserController extends GenericResourceController<User, UserService> {
    
    @Inject
    @Named("userService")
    @Override
    public void setService(UserService service) {
        this.service = service;
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/check")
    public Response checkCredentials(User user) {
        Boolean validCredentials = this.service.checkCredentials(user.getUsername(), user.getPassword());
        if(validCredentials) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

    }
}
