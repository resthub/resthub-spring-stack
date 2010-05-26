package org.resthub.identity.web.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

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
}
