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

import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.service.GroupService;
import org.resthub.identity.service.UserService;
import org.resthub.web.controller.GenericControllerImpl;

import com.sun.jersey.api.NotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.resthub.web.response.PageResponse;

/**
 * Front controller for Group Management<br/>
 * Only ADMINS can access to this API
 */
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Path("/group")
@Named("groupController")
public class GroupController extends GenericControllerImpl<Group, Long, GroupService> {

    /**
     * The userService <br/>
     * This should be a bean <br/>
     * This class need it to deal properly with user, eg to add a {@Link
     * User} to a {@Link Group}
     * 
     * */
    UserService userService;

    @Inject
    @Named("groupService")
    @Override
    public void setService(GroupService service) {
        this.service = service;
    }

    /**
     * Automatically called to inject the userService beans<br/>
     * This class need it to deal properly with user <br/>
     * 
     * @param userService
     *            the userService bean
     * */
    @Inject
    @Named("userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /** Override this methods in order to secure it **/
    @Override
    @POST
    @RolesAllowed({ "IM_GROUP_ADMIN" })
    public Group create(Group group) {
        return super.create(group);
    }

    /** Override this methods in order to secure it **/
    @Override
    @PUT
    @Path("/{id}")
    @RolesAllowed({ "IM_GROUP_ADMIN" })
    public Group update(@PathParam("id") Long id, Group group) {
        return super.update(id, group);
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @GET
    @Path("/all")
    @RolesAllowed({ "IM_GROUP_ADMIN", "IM_GROUP_READ" })
    public List<Group> findAll() {
        return super.findAll();
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @GET
    @RolesAllowed({ "IM_GROUP_ADMIN", "IM_GROUP_READ" })
    public PageResponse<Group> findAll(@QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size) {
        return super.findAll(page, size);
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @GET
    @Path("/{id}")
    @RolesAllowed({ "IM_GROUP_ADMIN", "IM_GROUP_READ" })
    public Group findById(@PathParam("id") Long id) {
        return super.findById(id);
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @DELETE
    @Path("/all")
    @RolesAllowed({ "IM_GROUP_ADMIN" })
    public void delete() {
        super.delete();
    }
    
    /** Override this methods in order to secure it **/
    @Override
    @DELETE
    @Path("/{id}")
    @RolesAllowed({ "IM_GROUP_ADMIN" })
    public void delete(@PathParam(value = "id") Long id) {
        super.delete(id);
    }

    /**
     * Find the group identified by the specified name.<br/>
     * 
     * @param name
     *            the name of the group
     * @return the group, in XML or JSON if the group can be found otherwise
     *         HTTP Error 404
     */
    @GET
    @Path("/name/{name}")
    public Group getGroupByName(@PathParam("name") String name) {
        Group group = this.service.findByName(name);
        if (group == null) {
            throw new NotFoundException();
        }
        return group;
    }

    /**
     * Gets the groups depending of the group
     * 
     * @param name
     *            the name of the group to search insides groups
     * 
     * @return a list of group, in XML or JSON if the group can be found
     *         otherwise HTTP Error 404
     */
    @GET
    @Path("/name/{name}/groups")
    public List<Group> getGroupsFromGroups(@PathParam("name") String name) {
        Group g = this.service.findByName(name);
        if (g == null) {
            throw new NotFoundException();
        }
        return g.getGroups();
    }

    /**
     * Puts a group inside the groups lists of one other group
     * 
     * @param name
     *            the name of the group in which we should add a group
     * @param group
     *            the name of the group the be added
     */
    @PUT
    @Path("/name/{name}/groups/{group}")
    public void addGroupToUser(@PathParam("name") String name, @PathParam("group") String group) {
        this.service.addGroupToGroup(name, group);
    }

    /**
     * Deletes a group from the groups lists of one other group
     * 
     * @param name
     *            the name of the group in which we should remove a group
     * @param group
     *            the name of the gorup the be removed
     */
    @DELETE
    @Path("/name/{name}/groups/{groups}")
    public void removeGroupsForUser(@PathParam("name") String name, @PathParam("groups") String groupName) {
        this.service.removeGroupFromGroup(name, groupName);
    }

    /**
     * Gets the permissions of one group
     * 
     * @param name
     *            the name of the group to search insides groups
     * 
     * @return a list of permissions, in XML or JSON if the group can be found
     *         otherwise HTTP Error 404
     */
    @GET
    @Path("/name/{name}/permissions")
    public List<String> getPermisionsFromGroup(@PathParam("name") String name) {
        List<String> permissions = this.service.getGroupDirectPermissions(name);
        if (permissions == null) {
            throw new NotFoundException();
        }
        return permissions;
    }

    /**
     * Add a permission to a group
     * 
     * @param name
     *            the name of the group in which we should add a group
     * @param permission
     *            the permission to be added
     */
    @PUT
    @Path("/name/{name}/permissions/{permission}")
    public void addPermissionsToUser(@PathParam("name") String login, @PathParam("permission") String permission) {
        this.service.addPermissionToGroup(login, permission);
    }

    /**
     * Remove a permission form one Group
     * 
     * @param name
     *            the name of the group in which we should remove a permission
     * @param permisssion
     *            the permission to be removed
     */
    @DELETE
    @Path("/name/{name}/permissions/{permission}")
    public void deletePermissionsFromUser(@PathParam("name") String name, @PathParam("permission") String permission) {
        this.service.removePermissionFromGroup(name, permission);
    }

    @GET
    @Path("/name/{name}/users")
    public List<User> getUsersFromGroup(@PathParam("name") String name) {
        List<User> usersFromGroup = this.userService.getUsersFromGroup(name);
        if (usersFromGroup == null) {
            throw new NotFoundException();
        }
        return usersFromGroup;
    }
    
    /**
     * {@inheritDoc}
     */
    @PUT
    @Path("/name/{name}/roles/{role}")
    @RolesAllowed({ "IM_GROUP_ADMIN" })
    public void addRoleToGroup(@PathParam("name") String name, @PathParam("role") String role) {
        this.service.addRoleToGroup(name, role);
    }

    /**
     * {@inheritDoc}
     */
    @DELETE
    @Path("/name/{name}/roles/{role}")
    @RolesAllowed({ "IM_GROUP_ADMIN" })
    public void removeRoleFromGroup(@PathParam("name") String name, @PathParam("role") String role) {
        this.service.removeRoleFromGroup(name, role);
    }

}
