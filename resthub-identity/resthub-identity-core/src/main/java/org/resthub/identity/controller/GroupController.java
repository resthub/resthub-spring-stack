package org.resthub.identity.controller;

import java.net.URI;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/group")
@RolesAllowed( { "ADMIN" })
@Named("groupController")
/**
 Front controller for Group Management<br/>
 Only ADMINS can access to this API
 */
public class GroupController extends
		GenericResourceController<Group, GroupService> {

	private final Logger logger = LoggerFactory
			.getLogger(GroupController.class);

	/**
	 * The userService <br/>
	 * This should be a bean <br/>
	 * This class need it to deal properly with user, eg to add a {@Link
	 * User} to a {@Link Group}
	 * 
	 * */
	UserService userService;

	@Inject
	@Named("groupService")
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void setService(GroupService service) {
		this.service = service;
	}

	/**
	 * Automatically called to inject the userService beans<br/>
	 * This class need it to deal properly with user <br/>
	 * 
	 * @param userService
	 *            the userService bean
	 * */
	@Inject
	@Named("userService")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Find the group identified by the specified name.<br/>
	 * 
	 * @param name
	 *            the name of the group
	 * @return the group, in XML or JSON if the group can be found otherwise
	 *         HTTP Error 404
	 */
	@GET
	@Path("/name/{name}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getGroupByName(@PathParam("name") String name) {
		Group group = this.service.findByName(name);
		Response r;
		r = (group == null) ? Response.status(Status.NOT_FOUND).entity(
				"Unable to find the requested group.").build() : Response.ok(
				group).build();
		return r;
	}

	/**
	 * Return the list of all groups.<br/>
	 * 
	 * @return the list of group, in XML or JSON * @return the group if their is
	 *         some groups defined otherwise HTTP Error 404
	 */
	@GET
	@Path("/all")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllGroups() {
		List<Group> result = this.service.findAllGroups();
		Response r;
		int size = (result == null) ? 0 : result.size();
		r = (size == 0) ? Response.status(Status.NOT_FOUND).entity(
				"Unable to find any group.").build() : Response.ok(result)
				.build();
		return r;
	}

	/**
	 * Return the list of all groups without including users.<br/>
	 * 
	 * @return a list of group, in XML or JSON if the group can be found
	 *         otherwise HTTP Error 404
	 */
	@GET
	@Path("/list")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllGroupsName() {
		List<Group> result = this.service.findAllGroups();
		int size = (result == null) ? 0 : result.size();
		Response r;
		if (size == 0) {
			r = Response.status(Status.NOT_FOUND).entity(
					"Unable to find any group.").build();
		} else {
			for (Group g : result) {
				g.setUsers(null);
			}
			r = Response.ok(result).build();
		}
		return r;
	}

	/**
	 * Remove a {@link User} from the specified {@link Group}.<br/>
	 * 
	 * @param name
	 *            the name of the group
	 * @param login
	 *            the user login
	 * @return the group updated after user removing if everything OK,in XML or
	 *         JSON, otherwise return an HTTP error 404
	 */
	@DELETE
	@Path("/{name}/user/{login}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response removeUser(@PathParam("name") String name,
			@PathParam("login") String login) {
		// TODO remove this trace which is in a standard behavior
		logger.info("Remove user '" + login + "' from group '" + name + "'.");

		Group group = this.service.findByName(name);
		User user = this.userService.findByLogin(login);
		Response r;
		if (group == null) {
			r = Response.status(Status.NOT_FOUND).entity(
					"Unable to find the requested group.").build();
		} else if (user == null) {
			r = Response.status(Status.NOT_FOUND).entity(
					"Unable to find the requested user.").build();
		} else {
			this.service.removeUser(group, user);
			r = Response.ok(group).build();
		}
		return r;
	}

	/**
	 * <p>Create a {@link Group} from the {@link Group} given in input.</br> If the given {@link Group} contains
	 * some {@link User}s, the {@link Group} will be added to the group list in which are the users
	 * belong</p>
	 * 
	 * @param group
	 *            the group to create, in XMl or JSON
	 * @return the created group, in XMl or JSON Goal : support creation of
	 *         group with user
	 */
	@POST
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	public Response create(Group group) {
		Group e = this.service.create(group);
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		URI uri = uriBuilder.path(generateIdentifierFromEntity(e).toString())
				.build();
		List<User> l = group.getUsers();
		if (userService != null && l != null) {
			for (User u : l) {
				User tmpUser = userService.findById(u.getId());
				if (tmpUser != null) {
					tmpUser.addToGroup(group);
					userService.update(tmpUser);
				}
			}
		}

		return Response.created(uri).entity(e).build();
	}
}