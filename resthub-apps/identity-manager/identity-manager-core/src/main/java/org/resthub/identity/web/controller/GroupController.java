package org.resthub.identity.web.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

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
}