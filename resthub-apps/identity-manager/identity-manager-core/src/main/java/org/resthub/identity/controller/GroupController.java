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

import org.resthub.identity.model.Group;
import org.resthub.identity.service.GroupService;
import org.resthub.web.controller.GenericResourceController;

@Path("/group")
@Named("groupController")
public class GroupController extends GenericResourceController<Group, GroupService> {

	@Inject
	@Named("groupService")
	@Override
	public void setService(GroupService service) {
		this.service = service;
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
}