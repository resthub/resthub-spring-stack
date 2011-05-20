package org.resthub.identity.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.service.RoleService;
import org.resthub.identity.service.UserService;
import org.resthub.web.controller.GenericControllerImpl;

import com.sun.jersey.api.NotFoundException;

/**
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
@Path("/role")
@RolesAllowed({"IM-ADMIN"})
@Named("roleController")
public class RoleController extends GenericControllerImpl<Role, Long, RoleService> {

    @Inject
    @Named("userService")
    protected UserService userService;

    @Inject
    @Named("roleService")
    @Override
    public void setService(RoleService service) {
        this.service = service;
    }

    /**
     * Gets all the users that have a role, direct or inherited.
     * @param filters A list of roles to look for.
     * @return A list of users having at least one of the roles defined as parameter.
     */
    @GET
    @Path("/{name}/users")
    @RolesAllowed({"IM-ADMIN"})
    public List<User> findAllUsersWithRole(@PathParam("name") String name) {
        List<User> usersWithRoles = this.userService.findAllUsersWithRoles(Arrays.asList(name));
        if (usersWithRoles == null) {
			throw new NotFoundException();
		}
        return usersWithRoles;
    }
}
