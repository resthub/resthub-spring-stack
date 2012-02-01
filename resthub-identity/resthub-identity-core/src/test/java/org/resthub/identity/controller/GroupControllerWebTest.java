package org.resthub.identity.controller;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.resthub.web.test.controller.AbstractControllerWebTest;
import org.springframework.http.MediaType;

/**
 * 
 * @author Guillaume Zurbach
 */
public class GroupControllerWebTest extends AbstractControllerWebTest<Group, Long> {
	

    @Override
	public void setUp() throws Exception {
		this.useOpenEntityManagerInViewFilter = true;
		super.setUp();
	}

	private String generateRandomGroupName() {
        return "GroupName" + Math.round(Math.random() * 10000000);
    }

    @Override
    protected Group createTestResource() {
        String groupName = this.generateRandomGroupName();
        Group g = new Group();
        g.setName(groupName);
        return g;
    }

    @Override
    protected String getResourcePath() {
        return "/api/group";
    }

    @Override
    protected Group udpateTestResource(Group r) {
        r.setName(this.generateRandomGroupName());
        return r;
    }

    @Override
    protected Long getResourceId(Group resource) {
        return resource.getId();
    }

    @Test
    public void testShouldGetUsersFromGroup() {
        /* Given a new group */
        String groupName = "testGroup";
        Group g = new Group();
        g.setName(groupName);

        g = resource().path("/api/group").type(MediaType.APPLICATION_JSON).post(Group.class, g);

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
        u = resource().path("/api/user").type(MediaType.APPLICATION_JSON).post(User.class, u);

        /* Given a link between this user and the group */
        resource().path("/api/user").path("name").path(u.getLogin()).path("groups").path(g.getName())
                .type(MediaType.APPLICATION_JSON).put();

        /* When I get the users of the group */
        String usersFromGroup = resource().path("/api/group").path("name").path(g.getName()).path("users")
                .accept(MediaType.APPLICATION_JSON).get(String.class);

        /* Then the list of users contains our user */
        assertTrue("The list of users should contain our just added user", usersFromGroup.contains(u.getLogin()));

        /* Cleanup */
        resource().path("/api/group").path(g.getId().toString()).type(MediaType.APPLICATION_JSON).delete();
        resource().path("/api/user").path("name").path(u.getLogin()).path("groups").path(g.getName()).type(MediaType.APPLICATION_JSON).delete();
        resource().path("/api/user").path(u.getId().toString()).type(MediaType.APPLICATION_JSON).delete();
    }
}
