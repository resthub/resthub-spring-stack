package org.resthub.identity.controller;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
public class GroupController extends GenericResourceController<Group, GroupService> {

	private final Logger logger = LoggerFactory.getLogger(GroupController.class);

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
	 * @param name
	 * @return group
	 */
	@GET
	@Path("/name/{name}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getUserByLogin( @PathParam("name") String name ) {
		Group group = this.service.findByName( name );
		if (group == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(group).build();
	}

	/**
	 * Find all groups.
	 * @return a list of group.
	 */
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getAllGroups() {
		List<Group> result = this.service.findAllGroups();
		return Response.ok(result).build();
	}

	/**
	 * Remove a user from the specified group.
	 * @param name the group name
	 * @param login the user login
	 * @return the group after the user removing
	 */
	@DELETE
	@Path("/{name}/user/{login}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response removeUser( @PathParam("name") String name,
								@PathParam("login") String login ) {

		logger.info("Remove user " + login + " from group " + name + ".");

		if( name != null && login != null )
		{
			Group group = this.service.findByName( name );
			User user = this.userService.findByLogin( login );

			this.service.removeUser( group, user );
			return Response.ok(group).build();
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
}