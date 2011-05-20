package org.resthub.identity.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.security.IdentityUserDetailsAdapter;
import org.resthub.identity.service.UserService;
import org.resthub.identity.tools.PermissionsOwnerTools;
import org.resthub.web.controller.GenericControllerImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sun.jersey.api.NotFoundException;

@Path("/user")
/**
Front controller for User Management<br/>
Only ADMINS can access to the globality of this API<br/>
Specific permissions are given when useful
 */
@Named("userController")
public class UserController extends GenericControllerImpl<User, Long, UserService> {

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
    @Override
    @RolesAllowed({"IM-ADMIN"})
    public List<User> findAll() {
    	return super.findAll();
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
    @RolesAllowed({"IM-ADMIN"})
    public User getUserByLogin(@PathParam("login") String login) {
        User user = this.service.findByLogin(login);
        if (user == null) {
			throw new NotFoundException();
		}
        return user;
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
    @RolesAllowed({"IM-USER", "IM-ADMIN"})
    public User currentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        IdentityUserDetailsAdapter userDetails = (IdentityUserDetailsAdapter) securityContext.getAuthentication().getPrincipal();

        User user = this.service.findByLogin(userDetails.getUsername());
        if (user == null) {
			throw new NotFoundException();
		}
        if (user != null) {
            List<String> permissions = PermissionsOwnerTools.getInheritedPermission(user);
            user.getPermissions().clear();
            user.getPermissions().addAll(permissions);
        }
        return user;
    }

    /**
     * Change the password of the user
     *
     * @param user
     *            the user with the new password inside
     * */
    @POST
    @Path("/password")
    @RolesAllowed({"IM-USER", "IM-ADMIN"})
    public User changePassword(User u) {
        User updatedUser = this.service.updatePassword(u);
        if (updatedUser == null) {
			throw new NotFoundException();
		}
        return updatedUser;
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
    public List<Group> getGroupsFromUser(@PathParam("login") String login) {
        User user = this.service.findByLogin(login);
        if (user == null) {
			throw new NotFoundException();
		}
        List<Group> groups = null;
        if (user != null) {
            groups = user.getGroups();
        }
        if (groups == null) {
			throw new NotFoundException();
		}
        return groups;
    }

    /**
     * Puts a group inside the groups lists of a user
     *
     * @Param login the login of the user for which we should add a group
     * @Param group the name of the group the be added
     */
    @PUT
    @Path("/name/{login}/groups/{group}")
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
    @RolesAllowed({"IM-ADMIN"})
    public List<String> getPermissionsFromUser(@PathParam("login") String login) {
        List<String> permissions = this.service.getUserPermissions(login);
        if (permissions == null) {
			throw new NotFoundException();
		}
        return permissions;
    }

    /**
     * Add a permission to a user
     *
     * @Param login the login of the user in which we should add a group
     * @Param permission the permission to be added
     */
    @PUT
    @Path("/name/{login}/permissions/{permission}")
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
    @POST
    @RolesAllowed({"IM-ADMIN"})
    public User create(User user) {
        return super.create(user);
    }

    /**
     * Used to create or update a user - The differences come from the service
     * layer
     *
     * @param user
     *            the user to create/update
     * */
    @Override
	@PUT
	@Path("/{id}")
    @RolesAllowed({"IM-ADMIN"})
    public User update(@PathParam("id") Long id, User user) {
        return super.update(id, user);
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
