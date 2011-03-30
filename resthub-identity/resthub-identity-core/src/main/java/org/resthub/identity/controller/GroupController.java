package org.resthub.identity.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.resthub.identity.service.GroupService;
import org.resthub.identity.service.UserService;
import org.resthub.web.controller.GenericResourceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/group")
@RolesAllowed({"IM-ADMIN"})
@Named("groupController")
/**
Front controller for Group Management<br/>
Only ADMINS can access to this API
 */
public class GroupController extends GenericResourceController<Group, GroupService> {

    private final Logger logger = LoggerFactory.getLogger(GroupController.class);
    @PersistenceContext
    protected EntityManager em;
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
    /**
     * {@inheritDoc}
     */
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getGroupByName(@PathParam("name") String name) {
        Group group = this.service.findByName(name);
        Response r;
        r = (group == null) ? Response.status(Status.NOT_FOUND).entity(
                "Unable to find the requested group.").build() : Response.ok(
                group).build();
        return r;
    }

    /**
     * Return the list of all groups.<br/>
     *
     * @return the list of group, in XML or JSON * @return the group if their is
     *         some groups defined otherwise HTTP Error 404
     */
    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAllGroups() {
        List<Group> result = this.service.findAllGroups();
        Response r;
        int size = (result == null) ? 0 : result.size();
        r = (size == 0) ? Response.status(Status.NOT_FOUND).entity(
                "Unable to find any group.").build() : Response.ok(result).build();
        return r;
    }

    /**
     * Return the list of all groups without including users.<br/>
     *
     * @return a list of group, in XML or JSON if the group can be found
     *         otherwise HTTP Error 404
     */
    @GET
    @Path("/list")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAllGroupsName() {
        List<Group> result = this.service.findAllGroups();
        int size = (result == null) ? 0 : result.size();
        Response r;
        if (size == 0) {
            r = Response.status(Status.NOT_FOUND).entity(
                    "Unable to find any group.").build();
        } else {
            r = Response.ok(result).build();
        }
        return r;
    }

    /**
     * Gets the groups depending of the group
     *
     *@param name
     *            the name of the group to search insides groups
     *
     * @return a list of group, in XML or JSON if the group can be found
     *         otherwise HTTP Error 404
     */
    @GET
    @Path("/name/{name}/groups")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getGroupsFromGroups(@PathParam("name") String name) {
        Group g = this.service.findByName(name);
        Response r = null;
        List<Group> groups = null;
        if (g != null) {
            groups = g.getGroups();
        }
        r = (groups == null) ? Response.status(Status.NOT_FOUND).entity(
                "Unable to find groups").build() : Response.ok(groups).build();
        return r;
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void addGroupToUser(@PathParam("name") String name,
            @PathParam("group") String group) {
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void removeGroupsForUser(@PathParam("name") String name,
            @PathParam("groups") String groupName) {
        this.service.removeGroupFromGroup(name, groupName);
    }

    /**
     * Gets the permissions of one group
     *
     *@param name
     *            the name of the group to search insides groups
     *
     *@return a list of permissions, in XML or JSON if the group can be found
     *         otherwise HTTP Error 404
     */
    @GET
    @Path("/name/{name}/permissions")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getPermisionsFromGroup(@PathParam("name") String name) {
        Response r = null;
        List<String> permissions = this.service.getGroupDirectPermissions(name);
        r = (permissions == null) ? Response.status(Status.NOT_FOUND).entity(
                "Unable to find groups").build() : Response.ok(permissions).build();
        return r;
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void addPermissionsToUser(@PathParam("name") String login,
            @PathParam("permission") String permission) {
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void deletePermissionsFromUser(@PathParam("name") String name,
            @PathParam("permission") String permission) {
        this.service.removePermissionFromGroup(name, permission);
    }

    @GET
    @Path("/name/{name}/users")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getUsersFromGroup(@PathParam("name") String name) {
        Response r = null;
        List<User> usersFromGroup = this.userService.getUsersFromGroup(name);
        r = (usersFromGroup == null) ? Response.status(Status.NOT_FOUND).build() : Response.ok(usersFromGroup).build();
        return r;
    }
}
