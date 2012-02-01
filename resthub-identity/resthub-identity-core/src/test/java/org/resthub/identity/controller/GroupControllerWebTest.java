package org.resthub.identity.controller;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.resthub.web.JsonHelper;
import org.resthub.web.test.controller.AbstractControllerWebTest;

import com.ning.http.client.Response;

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
    public void testShouldGetUsersFromGroup() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
        /* Given a new group */
        String groupName = "testGroup";
        Group g = new Group();
        g.setName(groupName);

        Response response = preparePost(getResourcePath()).setBody(JsonHelper.serialize(g)).execute().get();
        
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
        response = preparePost("/api/user").setBody(JsonHelper.serialize(u)).execute().get();
        u = JsonHelper.deserialize(response.getResponseBody(), User.class);
        
        /* Given a link between this user and the group */
        preparePut("/api/user/name/" + u.getLogin() + "/groups/" + g.getName()).execute().get();
        
        /* When I get the users of the group */
        Response responce = prepareGet(getResourcePath() + "/name/" + g.getName() + "/users").execute().get(); 
        String usersFromGroup = responce.getResponseBody();		

        /* Then the list of users contains our user */
        assertTrue("The list of users should contain our just added user", usersFromGroup.contains(u.getLogin()));

        /* Cleanup */
        prepareDelete(getResourcePath() + g.getId()).execute().get();
        prepareDelete("/api/user/name/" + u.getLogin() + "/groups/" + g.getName()).execute().get();
        prepareDelete("/api/user/" + u.getId()).execute().get();
        
    }
}
