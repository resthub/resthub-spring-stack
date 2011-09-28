package org.resthub.identity.controller;

import java.util.Arrays;
import java.util.List;

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
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import org.resthub.web.response.PageResponse;

/**
 * 
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
@Path("/role")
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
    
    /** Override this methods in order to secure it **/
    @Override
    @POST
    @RolesAllowed({ "IM_ROLE_ADMIN" })
    public Role create(Role role) {
        return super.create(role);
    }

    /** Override this methods in order to secure it **/
    @Override
    @PUT
    @Path("/{id}")
    @RolesAllowed({ "IM_ROLE_ADMIN" })
    public Role update(@PathParam("id") Long id, Role role) {
        return super.update(id, role);
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @GET
    @Path("/all")
    @RolesAllowed({ "IM_ROLE_ADMIN", "IM_ROLE_READ" })
    public List<Role> findAll() {
        return super.findAll();
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @GET
    @RolesAllowed({ "IM_ROLE_ADMIN", "IM_ROLE_READ" })
    public PageResponse<Role> findAll(@QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size) {
        return super.findAll(page, size);
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @GET
    @Path("/{id}")
    @RolesAllowed({ "IM_ROLE_ADMIN", "IM_ROLE_READ" })
    public Role findById(@PathParam("id") Long id) {
        return super.findById(id);
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @DELETE
    @Path("/all")
    @RolesAllowed({ "IM_ROLE_ADMIN" })
    public void delete() {
        super.delete();
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @DELETE
    @Path("/{id}")
    @RolesAllowed({ "IM_ROLE_ADMIN" })
    public void delete(@PathParam(value = "id") Long id) {
        super.delete(id);
    }

    /**
     * Gets all the users that have a role, direct or inherited.
     * 
     * @param filters
     *            A list of roles to look for.
     * @return A list of users having at least one of the roles defined as
     *         parameter.
     */
    @GET
    @Path("/{name}/users")
    @RolesAllowed({ "IM-ADMIN" })
    public List<User> findAllUsersWithRole(@PathParam("name") String name) {
        List<User> usersWithRoles = this.userService.findAllUsersWithRoles(Arrays.asList(name));
        if (usersWithRoles == null) {
            throw new NotFoundException();
        }
        return usersWithRoles;
    }
}
