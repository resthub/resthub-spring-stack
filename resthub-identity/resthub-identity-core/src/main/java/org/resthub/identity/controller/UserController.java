package org.resthub.identity.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
public class UserController extends
		GenericResourceController<User, UserService> {

	@Inject
	@Named("userService")
	@Override
	public void setService(UserService service) {
		this.service = service;
	}

	@GET
	@Path("/all")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllUsers() {

       List<User> users=  this.service.findAll();
        return Response.ok(users).build();
		
	}
	
	/**
	 * Find the user identified by the specified login.
	 * 
	 * @param login
	 * @return user
	 */
	@GET
	@Path("/login/{login}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUserByLogin(@PathParam("login") String login) {
		User user = this.service.findByLogin(login);
		if (user == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(user).build();
	}

	/**
	 * Check Login and Password from the user
	 * 
	 * @FormParam Login, Password ,<Redirect>
	 * @return user if authentication successed if Redirect not defined
	 * @return HTTP_REDIRECT to Redirect if authentication successed and
	 *         Redirect well defined
	 * @return HTTP_FORBIDDEN if authentication failed
	 * @return HTTP_NOT_ACCEPTABLE if authentication successed and Redirect is
	 *         not acceptable
	 */
	@POST
	@Path("/login")
	public Response authenticateUser(@FormParam("Login") String login,
			@FormParam("Password") String password,
			@FormParam("Redirect") String redirect) {
		Response r = null;

		User u = service.authenticateUser(login, password);

		if (u == null) {
			r = Response.status(Status.FORBIDDEN).build();
		} else {
			if (redirect == null) {
				r = Response.ok(u).build();
			} else {
				try {

					r = Response.temporaryRedirect(new URI(redirect)).build();
				} catch (URISyntaxException e) {
					r = Response.status(Status.NOT_ACCEPTABLE).build();
				}
			}
		}
		return r;
	}

	@POST
	@Path("/login2")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response authenticateUser(User pU) {
		Response r = null;
		User u = service.authenticateUser(pU.getLogin(), pU.getPassword());
		r = (u == null) ? 
				Response.status(Status.FORBIDDEN).build() : 
				Response.ok(u).build();
		return r;
	}
}
