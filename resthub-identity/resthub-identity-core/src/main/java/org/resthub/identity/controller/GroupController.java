package org.resthub.identity.controller;

import java.net.URI;
import java.util.List;

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
@Named("groupController")
public class GroupController extends
		GenericResourceController<Group, GroupService> {

	private final Logger logger = LoggerFactory
			.getLogger(GroupController.class);

	UserService userService;

	@Inject
	@Named("groupService")
	@Override
	public void setService(GroupService service) {
		this.service = service;
	}

	@Inject
	@Named("userService")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Find the group identified by the specified name.
	 * 
	 * @param name
	 * @return group
	 */
	@GET
	@Path("/name/{name}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getGroupByName(@PathParam("name") String name) {
		Group group = this.service.findByName(name);
		if (group == null) {
			return Response.status(Status.NOT_FOUND).entity(
					"Unable to find the requested group.").build();
		}
		return Response.ok(group).build();
	}

	/**
	 * Find all groups.
	 * 
	 * @return a list of group.
	 */
	@GET
	@Path("/all")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllGroups() {
		List<Group> result = this.service.findAllGroups();
		return Response.ok(result).build();
	}

	/**
	 * Find all groups without showing users.
	 * 
	 * @return a list of group.
	 */
	@GET
	@Path("/list")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllGroupsName() {
		List<Group> result = this.service.findAllGroups();
		for (Group g : result) {
			g.setUsers(null);
		}
		return Response.ok(result).build();
	}

	/**
	 * Remove a user from the specified group.
	 * 
	 * @param name
	 *            the group name
	 * @param login
	 *            the user login
	 * @return the group updated after user removing
	 */
	@DELETE
	@Path("/{name}/user/{login}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response removeUser(@PathParam("name") String name,
			@PathParam("login") String login) {

		logger.info("Remove user '" + login + "' from group '" + name + "'.");

		Group group = this.service.findByName(name);
		User user = this.userService.findByLogin(login);

		if (group == null) {
			return Response.status(Status.NOT_FOUND).entity(
					"Unable to find the requested group.").build();
		}
		if (user == null) {
			return Response.status(Status.NOT_FOUND).entity(
					"Unable to find the requested user.").build();
		} else {
			this.service.removeUser(group, user);
			return Response.ok(group).build();

		}
	}

	/* Goal : support creation of group with user */
	@POST
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	public Response create(Group entity) {
		Group e = this.service.create(entity);
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		URI uri = uriBuilder.path(generateIdentifierFromEntity(e).toString())
				.build();
		if (userService != null && entity.getUsers()!=null) {
			for (User u : entity.getUsers()) {
				User tmpUser = userService.findById(u.getId());
				if(tmpUser!=null){
				tmpUser.addGroup(entity);
				userService.update(tmpUser);}
			}
		}

		return Response.created(uri).entity(e).build();
	}
}