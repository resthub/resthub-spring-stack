package org.resthub.identity.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.security.IdentityUserDetailsAdapter;
import org.resthub.identity.service.UserService;
import org.resthub.identity.tools.PermissionsOwnerTools;
import org.resthub.web.controller.GenericResourceController;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

@Path("/user")
/**
Front controller for User Management<br/>
Only ADMINS can access to the globality of this API<br/>
Specific permissions are given when useful
 */
@Named("userController")
public class UserController extends GenericResourceController<User, UserService> {

    @Inject
    @Named("userService")
    @Override
    /**
     * {@inheritDoc}
     * */
    public void setService(UserService service) {
        this.service = service;
    }

    /**
     * Return a list of all users
     *
     * @return a list of users, in XML or JSON if users can be found otherwise
     *         HTTP Error 404
     */
    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"IM-ADMIN"})
    public Response getAllUsers() {

        List<User> users = this.service.findAll();
        Response r;
        int size = (users == null) ? 0 : users.size();
        if (size == 0) {
            r = Response.status(Status.NOT_FOUND).entity("Unable to find users").build();
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"IM-ADMIN"})
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"IM-USER", "IM-ADMIN"})
    public Response currentUser() {
    	SecurityContext securityContext = SecurityContextHolder.getContext();
    	IdentityUserDetailsAdapter userDetails = (IdentityUserDetailsAdapter)securityContext.getAuthentication().getPrincipal();

        User user = this.service.findByLogin(userDetails.getUsername());
        Response response = Response.status(Status.NOT_FOUND).build();
        if (user != null) {
            List<String> permissions = PermissionsOwnerTools.getInheritedPermission(user);
            user.getPermissions().clear();
            user.getPermissions().addAll(permissions);
            response = Response.ok(user).build();
        }
        return response;
    }

    /**
     * Change the password of the user
     *
     * @param user
     *            the user with the new password inside
     * */
    @POST
    @Path("/password")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"IM-USER", "IM-ADMIN"})
    public Response changePassword(User u) {
        u = this.service.updatePassword(u);
        Response r;
        r = (u == null) ? Response.status(Status.NOT_FOUND).entity(
                "Unable to save the user").build() : Response.ok(u).build();
        return r;
    }

    /**
     * Gets the groups depending of the user
     *
     *@Param login the login of the user to search insides groups
     *
     * @return a list of group, in XML or JSON if the group can be found
     *         otherwise HTTP Error 404
     */
    @GET
    @Path("/name/{login}/groups")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"IM-ADMIN"})
    public Response getGroupsFromUser(@PathParam("login") String login) {
        User user = this.service.findByLogin(login);
        Response r = null;
        List<Group> groups = null;
        if (user != null) {
            groups = user.getGroups();
        }
        r = (groups == null) ? Response.status(Status.NOT_FOUND).entity(
                "Unable to find groups").build() : Response.ok(groups).build();
        return r;
    }

    /**
     * Puts a group inside the groups lists of a user
     *
     * @Param login the login of the user for which we should add a group
     * @Param group the name of the group the be added
     */
    @PUT
    @Path("/name/{login}/groups/{group}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"IM-ADMIN"})
    public void addGroupToUser(@PathParam("login") String login,
            @PathParam("group") String group) {
        this.service.addGroupToUser(login, group);
    }

    /**
     * Deletes a group from the groups lists of a user
     *
     * @Param login the login of the user for which we should remove a group
     * @Param group the name of the group the be removed
     */
    @DELETE
    @Path("/name/{login}/groups/{groups}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"IM-ADMIN"})
    public void removeGroupsForUser(@PathParam("login") String userLogin,
            @PathParam("groups") String groupName) {
        this.service.removeGroupFromUser(userLogin, groupName);
    }

    /**
     * Gets the permissions of a user
     *
     *@Param login the login of the user to search insides groups
     *
     * @return a list of permissions, in XML or JSON if the group can be found
     *         otherwise HTTP Error 404
     */
    @GET
    @Path("/name/{login}/permissions")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"IM-ADMIN"})
    public Response getPermissionsFromUser(@PathParam("login") String login) {
        Response r = null;
        List<String> permissions = this.service.getUserPermissions(login);
        r = (permissions == null) ? Response.status(Status.NOT_FOUND).entity(
                "Unable to find permissions").build() : Response.ok(permissions).build();
        return r;
    }

    /**
     * Add a permission to a user
     *
     * @Param login the login of the user in which we should add a group
     * @Param permission the permission to be added
     */
    @PUT
    @Path("/name/{login}/permissions/{permission}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"IM-ADMIN"})
    public void addPermissionsToUser(@PathParam("login") String login,
            @PathParam("permission") String permission) {
        this.service.addPermissionToUser(login, permission);
    }

    /**
     * Remove a permisssion for one User
     *
     * @Param login the login of the user in which we should remove a permission
     * @Param permisssion the permisssion to be removed
     */
    @DELETE
    @Path("/name/{login}/permissions/{permission}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"IM-ADMIN"})
    public void deletePermissionsFromUser(@PathParam("login") String login,
            @PathParam("permission") String permission) {
        this.service.removePermissionFromUser(login, permission);
    }

    /**
     * Used to create or update a user - The differences come from the service
     * layer
     *
     * @param user
     *            the user to create/update
     * */
    @Override
    @RolesAllowed({"IM-ADMIN"})
    public Response create(User user) {
        User u = service.create(user);
        Response r;
        r = (u == null) ? Response.status(Status.NOT_FOUND).entity(
                "Unable to save the user").build() : Response.ok(u).build();
        return r;
    }

    /**
     * {@inheritDoc}
     */
    @PUT
    @Path("/name/{login}/roles/{role}")
    @RolesAllowed({"IM-ADMIN"})
    public void addRoleToUser(@PathParam("login") String login, @PathParam("role") String role) {
        this.service.addRoleToUser(login, role);
    }

    /**
     * {@inheritDoc}
     */
    @DELETE
    @Path("/name/{login}/roles/{role}")
    @RolesAllowed({"IM-ADMIN"})
    public void removeRoleFromUser(@PathParam("login") String login, @PathParam("role") String role) {
        this.service.removeRoleFromUser(login, role);
    }

    /**
     * Get all the roles of a user.
     * @param login Login to check.
     * @return A list of roles the given user has.
     */
    @GET
    @Path("/name/{login}/roles")
    @RolesAllowed({"IM-ADMIN"})
    public List<Role> getAllUserRoles(@PathParam("login") String login) {
        return this.service.getAllUserRoles(login);
    }
}
