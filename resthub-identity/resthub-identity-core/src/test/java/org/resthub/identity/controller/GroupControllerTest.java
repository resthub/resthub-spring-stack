package org.resthub.identity.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;

import junit.framework.Assert;
import static org.junit.Assert.assertTrue;

import org.resthub.identity.model.Group;
import org.resthub.identity.service.GroupService;
import org.resthub.web.test.controller.AbstractResourceControllerTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;
import org.junit.Test;
import org.resthub.identity.model.User;

/**
 * 
 * @author Guillaume Zurbach
 */
public class GroupControllerTest
        extends AbstractResourceControllerTest<Group, GroupService, GroupController> {

    @Override
    @Inject
    public void setController(GroupController groupController) {
        super.setController(groupController);
    }

    @Override
    protected Group createTestResource() throws Exception {
        String groupName = "GroupTestGroupName" + Math.round(Math.random() * 1000);
        Group g = new Group();
        g.setName(groupName);
        return g;
    }
    GroupService groupService;

    @Inject
    @Named("groupService")
    public void setGrouPService(GroupService gs) {
        this.groupService = gs;
    }

    @Override
    public void testUpdate() throws Exception {
        Group u1 = new Group();
        u1.setName("u1");

        WebResource r = resource().path("group");
        u1 = r.type(MediaType.APPLICATION_XML).post(Group.class, u1);
        r = resource().path("group/" + u1.getId());
        Group u2 = u1;
        u2.setName("u2");
        // Update name
        ClientResponse cr = r.type(MediaType.APPLICATION_XML).accept(
                MediaType.APPLICATION_JSON).put(ClientResponse.class, u2);
        Assert.assertEquals("Group not updated",
                Status.CREATED.getStatusCode(), cr.getStatus());
        String response = resource().path("group").accept(
                MediaType.APPLICATION_JSON).get(String.class);
        Assert.assertFalse("Group not updated", response.contains("u1"));
        Assert.assertTrue("Group not updated", response.contains("u2"));
    }

    @Test
    public void testShouldGetUsersFromGroup() {
        /* Given a new group */
        String groupName = "testGroup";
        Group g = new Group();
        g.setName(groupName);

        g = resource().path("group").type(MediaType.APPLICATION_XML).post(Group.class, g);

        /* Given a new user */
        String firstName = "first";
        String lastName = "last";
        String password = "pass";
        String login = "testLogin";

        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setPassword(password);
        u.setLogin(login);
        u = resource().path("user").type(MediaType.APPLICATION_XML).post(User.class, u);

        /* Given a link between this user and the group */
        resource().path("user").path("name").path(u.getLogin()).path("groups").path(g.getName()).type(MediaType.APPLICATION_XML).put();

        /* When I get the users of the group */
        String usersFromGroup = resource().path("group").path("name").path(g.getName()).path("users").accept(MediaType.APPLICATION_JSON).get(String.class);

        /* Then the list of users contains our user */
        assertTrue("The list of users should contain our just added user", usersFromGroup.contains(u.getLogin()));

        /* Cleanup */
        resource().path("user").path("name").path(u.getLogin()).path("groups").path(g.getName()).type(MediaType.APPLICATION_XML).delete();
        resource().path("user").path(u.getId().toString()).type(MediaType.APPLICATION_XML).delete();
        resource().path("group").path(g.getId().toString()).type(MediaType.APPLICATION_XML).delete();
    }
}
