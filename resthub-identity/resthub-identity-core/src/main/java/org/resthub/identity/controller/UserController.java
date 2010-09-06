package org.resthub.identity.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.resthub.identity.model.User;
import org.resthub.identity.service.UserService;
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

	/**
	 * Find the user identified by the specified login.
	 * @param login
	 * @return user
	 */
	@GET
	@Path("/login/{login}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getUserByLogin( @PathParam("login") String login ) {
		User user = this.service.findByLogin( login );
		if (user == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(user).build();
	}
}
