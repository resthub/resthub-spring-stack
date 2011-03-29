package org.resthub.identity.service.acl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.resthub.identity.model.Group;

@Path("/secured")
@Named("securedGroupController")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class SecuredGroupController {
	
	@Inject
	protected SecuredGroupService groupService;
	
	@Inject
	protected AclService aclService;
	
	@POST
	public Response create(Group group, @HeaderParam("user_id") String userId) {
		groupService.create(group);
		// Adds acl
		aclService.saveAcl(group, group.getId(), userId, "CUSTOM");
		return Response.ok(group).build();
	}
	
	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") Long groupId) {
		groupService.delete(groupService.getById(groupId));
	}
}
