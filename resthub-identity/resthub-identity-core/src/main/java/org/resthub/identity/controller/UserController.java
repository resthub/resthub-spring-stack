package org.resthub.identity.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.service.UserService;
import org.resthub.identity.tools.PermissionsOwnerTools;
import org.resthub.web.controller.GenericControllerImpl;
import org.resthub.web.response.PageResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.uri.UriComponent;

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

    /** Override this methods in order to secure it **/
    @Override
    @POST
    @RolesAllowed({ "IM_USER_ADMIN" })
    public User create(User user) {
        return super.create(user);
    }

    /** Override this methods in order to secure it **/
    @Override
    @PUT
    @Path("/{id}")
    @RolesAllowed({ "IM_USER_ADMIN" })
    public User update(@PathParam("id") Long id, User user) {
        return super.update(id, user);
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @GET
    @Path("/all")
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" })
    public List<User> findAll() {
        return super.findAll();
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @GET
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" })
    public PageResponse<User> findAll(@QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size) {
        return super.findAll(page, size);
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @GET
    @Path("/{id}")
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" })
    public User findById(@PathParam("id") Long id) {
        return super.findById(id);
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @DELETE
    @Path("/all")
    @RolesAllowed({ "IM_USER_ADMIN" })
    public void delete() {
        super.delete();
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @DELETE
    @Path("/{id}")
    @RolesAllowed({ "IM_USER_ADMIN" })
    public void delete(@PathParam(value = "id") Long id) {
        super.delete(id);
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
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" })
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
    @RolesAllowed({ "IS_AUTHENTICATED_FULLY" })
    public User currentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
        Assert.notNull(userDetails);
        
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
    
    /** Update the current user **/
    @PUT
    @Path("/me")
    @RolesAllowed({ "IS_AUTHENTICATED_FULLY" })
    public User updateMe(User user) {
    	SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
    	Assert.notNull(userDetails);
    	Assert.isTrue(userDetails.getUsername().equals(user.getLogin()));
    	User retreivedUser = this.service.findByLogin(userDetails.getUsername());

        if (retreivedUser == null) {
            throw new NotFoundException();
        }
        
        retreivedUser = super.update(retreivedUser.getId(), user);
        
        return retreivedUser;
    }

    /**
     * Gets the groups depending of the user
     * 
     * @Param login the login of the user to search insides groups
     * 
     * @return a list of group, in XML or JSON if the group can be found
     *         otherwise HTTP Error 404
     */
    @GET
    @Path("/name/{login}/groups")
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" })
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
    @RolesAllowed({ "IM_USER_ADMIN" })
    public void addGroupToUser(@PathParam("login") String login, @PathParam("group") String group) {
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
    @RolesAllowed({ "IM_USER_ADMIN" })
    public void removeGroupsForUser(@PathParam("login") String userLogin, @PathParam("groups") String groupName) {
        this.service.removeGroupFromUser(userLogin, groupName);
    }

    /**
     * Gets the permissions of a user
     * 
     * @Param login the login of the user to search insides groups
     * 
     * @return a list of permissions, in XML or JSON if the group can be found
     *         otherwise HTTP Error 404
     */
    @GET
    @Path("/name/{login}/permissions")
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" })
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
    @RolesAllowed({ "IM_USER_ADMIN" })
    public void addPermissionsToUser(@PathParam("login") String login, @PathParam("permission") String permission) {
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
    @RolesAllowed({ "IM_USER_ADMIN" })
    public void deletePermissionsFromUser(@PathParam("login") String login, @PathParam("permission") String permission) {
        this.service.removePermissionFromUser(login, permission);
    }

    /**
     * {@inheritDoc}
     */
    @PUT
    @Path("/name/{login}/roles/{role}")
    @RolesAllowed({ "IM_USER_ADMIN" })
    public void addRoleToUser(@PathParam("login") String login, @PathParam("role") String role) {
        this.service.addRoleToUser(login, role);
    }

    /**
     * {@inheritDoc}
     */
    @DELETE
    @Path("/name/{login}/roles/{role}")
    @RolesAllowed({ "IM_USER_ADMIN" })
    public void removeRoleFromUser(@PathParam("login") String login, @PathParam("role") String role) {
        this.service.removeRoleFromUser(login, role);
    }

    /**
     * Get all the roles of a user.
     * 
     * @param login
     *            Login to check.
     * @return A list of roles the given user has.
     */
    @GET
    @Path("/name/{login}/roles")
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" })
    public List<Role> getAllUserRoles(@PathParam("login") String login) {
        return this.service.getAllUserRoles(login);
    }

    /**
     * Check the user identity with the given user name and password.
     * 
     * @param username
     *            The user name.
     * @param password
     *            The password of the user.
     * @return True of false whether the user provided a correct identity or
     *         not.
     */
    @POST
    @Path("/checkuser")
    public void authenticateUser(@QueryParam("user") String username, @QueryParam("password") String password) {
        
    	// We decode manually parameters since they are not decoded automatically
    	// Perhaps it comes from the fact we are using a POST and not a GET request, not sure ...
    	// Related issue : http://java.net/jira/browse/JERSEY-739
    	
    	username = UriComponent.decode(username, UriComponent.Type.QUERY_PARAM);
    	password = UriComponent.decode(password, UriComponent.Type.QUERY_PARAM);
    	
    	User user = this.service.authenticateUser(username, password);
        if (user == null) {
            throw new NotFoundException();
        }
    }
}
