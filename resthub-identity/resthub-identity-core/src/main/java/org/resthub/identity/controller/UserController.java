package org.resthub.identity.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.service.UserService;
import org.resthub.identity.tools.PermissionsOwnerTools;
import org.resthub.web.controller.GenericControllerImpl;
import org.resthub.web.exception.NotFoundException;
import org.resthub.web.response.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 Front controller for User Management<br/>
 Only ADMINS can access to the globality of this API<br/>
 Specific permissions are given when useful
 */
@Controller @RequestMapping("/api/user")
public class UserController extends GenericControllerImpl<User, Long, UserService> {

    @Inject @Named("userService") @Override
    public void setService(UserService service) {
        this.service = service;
    }

    /** Override this methods in order to secure it **/
    @RolesAllowed({ "IM_USER_ADMIN" }) @Override
    public User create(@RequestBody User user) {
        return super.create(user);
    }

    /** Override this methods in order to secure it **/
    @RolesAllowed({ "IM_USER_ADMIN" }) @Override
    public User update(@PathVariable("id") Long id, @RequestBody User user) {
        return super.update(id, user);
    }
    
    /** Override this methods in order to secure it **/
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" }) @Override
    public List<User> findAll() {
        return super.findAll();
    }
    
    /** Override this methods in order to secure it **/
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" }) @RequestMapping(method = RequestMethod.GET) @Override
    public PageResponse<User> findAll(@QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size) {
        return super.findAll(page, size);
    }
    
    /** Override this methods in order to secure it **/
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" }) @Override
    public User findById(@PathVariable("id") Long id) {
        return super.findById(id);
    }
    
    /** Override this methods in order to secure it **/
    @RolesAllowed({ "IM_USER_ADMIN" }) @Override
    public void delete() {
        super.delete();
    }
    
    /** Override this methods in order to secure it **/
    @RolesAllowed({ "IM_USER_ADMIN" }) @Override
    public void delete(@PathVariable(value = "id") Long id) {
        super.delete(id);
    }

    /**
     * Return the user identified by the specified login.
     * 
     * @param login
     * @return the user, in XML or JSON if the user can be found otherwise HTTP
     *         Error 404
     */
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" }) @RequestMapping(method = RequestMethod.GET, value = "login/{login}") @ResponseBody
    public User getUserByLogin(@PathVariable("login") String login) {
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
    @RolesAllowed({ "IS_AUTHENTICATED_FULLY" }) @RequestMapping(method = RequestMethod.GET, value = "me") @ResponseBody
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
    @RolesAllowed({ "IS_AUTHENTICATED_FULLY" }) @RequestMapping(method = RequestMethod.PUT, value = "me") @ResponseBody
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
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" }) @RequestMapping(method = RequestMethod.GET, value = "name/{login}/groups") @ResponseBody
    public List<Group> getGroupsFromUser(@PathVariable("login") String login) {
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
    @RolesAllowed({ "IM_USER_ADMIN" }) @RequestMapping(method = RequestMethod.PUT, value = "name/{login}/groups/{group}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addGroupToUser(@PathVariable("login") String login, @PathVariable("group") String group) {
        this.service.addGroupToUser(login, group);
    }

    /**
     * Deletes a group from the groups lists of a user
     * 
     * @Param login the login of the user for which we should remove a group
     * @Param group the name of the group the be removed
     */
    @RolesAllowed({ "IM_USER_ADMIN" }) @RequestMapping(method = RequestMethod.DELETE, value = "name/{login}/groups/{groups}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGroupsForUser(@PathVariable("login") String userLogin, @PathVariable("groups") String groupName) {
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
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" }) @RequestMapping(method = RequestMethod.GET, value = "name/{login}/permissions") @ResponseBody
    public List<String> getPermissionsFromUser(@PathVariable("login") String login) {
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
    @RolesAllowed({ "IM_USER_ADMIN" }) @RequestMapping(method = RequestMethod.PUT, value = "name/{login}/permissions/{permission}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPermissionsToUser(@PathVariable("login") String login, @PathVariable("permission") String permission) {
        this.service.addPermissionToUser(login, permission);
    }

    /**
     * Remove a permisssion for one User
     * 
     * @Param login the login of the user in which we should remove a permission
     * @Param permisssion the permisssion to be removed
     */
    @RolesAllowed({ "IM_USER_ADMIN" }) @RequestMapping(method = RequestMethod.DELETE, value = "name/{login}/permissions/{permission}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePermissionsFromUser(@PathVariable("login") String login, @PathVariable("permission") String permission) {
        this.service.removePermissionFromUser(login, permission);
    }

    @RolesAllowed({ "IM_USER_ADMIN" }) @RequestMapping(method = RequestMethod.PUT, value = "name/{login}/roles/{role}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addRoleToUser(@PathVariable("login") String login, @PathVariable("role") String role) {
        this.service.addRoleToUser(login, role);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAllowed({ "IM_USER_ADMIN" }) @RequestMapping(method = RequestMethod.DELETE, value = "name/{login}/roles/{role}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRoleFromUser(@PathVariable("login") String login, @PathVariable("role") String role) {
        this.service.removeRoleFromUser(login, role);
    }

    /**
     * Get all the roles of a user.
     * 
     * @param login
     *            Login to check.
     * @return A list of roles the given user has.
     */
    @RolesAllowed({ "IM_USER_ADMIN", "IM_USER_READ" }) @RequestMapping(method = RequestMethod.GET, value = "name/{login}/roles") @ResponseBody 
    public List<Role> getAllUserRoles(@PathVariable("login") String login) {
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
    @RequestMapping(method = RequestMethod.POST, value = "checkuser") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void authenticateUser(@RequestParam("user") String username, @RequestParam("password") String password) {
           	
    	User user = this.service.authenticateUser(username, password);
        if (user == null) {
            throw new NotFoundException();
        }
    }
}
