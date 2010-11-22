package org.resthub.identity.controller;

import java.net.URI;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.Status;

import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.resthub.identity.service.GroupService;
import org.resthub.identity.service.UserService;
import org.resthub.web.controller.GenericResourceController;

@Path("/user")
@RolesAllowed( { "IM-ADMIN" })
/**
 Front controller for User Management<br/>
 Only ADMINS can access to the globality of this API<br/>
 Specific permissions are given when useful
 */
@Named("userController")
public class UserController extends
		GenericResourceController<User, UserService> {

	@Inject
	@Named("userService")
	@Override
	/**
	 * {@inheritDoc} 
	 * */
	public void setService(UserService service) {
		this.service = service;
	}

	GroupService groupeService;

	@Inject
	@Named("groupService")
	public void setGroupService(GroupService g) {
		groupeService = g;
	}

	/**
	 * Return a list of all users
	 * 
	 * @return a list of users, in XML or JSON if users can be found otherwise
	 *         HTTP Error 404
	 */

	@GET
	@Path("/all")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllUsers() {

		List<User> users = this.service.findAll();
		Response r;
		int size = (users == null) ? 0 : users.size();
		if (size == 0) {
			r = Response.status(Status.NOT_FOUND)
					.entity("Unable to find users").build();
		} else {
			r = Response.ok(users).build();
		}
		return r;

	}

	/**
	 * Return the user identified by the specified login.
	 * 
	 * @param login
	 * @return the user, in XML or JSON if the user can be found otherwise HTTP
	 *         Error 404
	 */
	@GET
	@Path("/login/{login}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUserByLogin(@PathParam("login") String login) {
		User user = this.service.findByLogin(login);
		Response r;
		r = (user == null) ? Response.status(Status.NOT_FOUND).entity(
				"Unable to find the user").build() : Response.ok(user).build();
		return r;
	}

	/**
	 * Return the currently authentified Used<br/>
	 * 
	 * <p>
	 * This is the first method to call once authenticated with Oauth2
	 * Currently, the Oauth2 authentication method is the one remaining We can't
	 * be log without using OAuth2 The user_id will be override by the filter
	 * layer, so we can't get the User object corresponding to another user than
	 * the one logged
	 * </p>
	 * 
	 * @param login
	 *            , given by the filter layer, once the token has been checked
	 * @return the Logged User Object, in XMl or JSON type if everything OK,
	 *         otherwise (It shouldn't append) an HTTP error 404
	 * */
	@GET
	@Path("/me")
	@RolesAllowed( { "IM-USER" })
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response currentUser(@HeaderParam("user_id") String login) {
		User u = this.service.findByLogin(login);
		Response r;
		r = (u == null) ? Response.status(Status.NOT_FOUND).build() : Response
				.ok(u).build();
		return r;
	}

	@Override
	@POST
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response create(User user) {
		User u = this.service.create(user);

		List<Group> lg = user.getGroups();
		if (lg != null) {
			for (Group g : lg) {
				if (g != null) {
					if (g.getId() != null) {
						g = groupeService.findById(g.getId());
						g.addUser(u);
						groupeService.update(g);
					
					}
				}
			}
		}
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		URI uri = uriBuilder.path(generateIdentifierFromEntity(u).toString())
				.build();

		return Response.created(uri).entity(u).build();
	}
}
