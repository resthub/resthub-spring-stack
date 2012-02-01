package org.resthub.identity.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.model.UserWithPassword;
import org.resthub.web.Http;
import org.resthub.web.JsonHelper;
import org.resthub.web.test.controller.AbstractControllerWebTest;

import com.ning.http.client.Response;

/**
 * 
 * @author Guillaume Zurbach
 */
public class UserControllerWebTest extends AbstractControllerWebTest<User, Long> {

    Logger logger = Logger.getLogger(UserControllerWebTest.class);
    
    @Override
	public void setUp() throws Exception {
		this.useOpenEntityManagerInViewFilter = true;
		super.setUp();
	}

    private String generateRandomLogin() {
        return "Login" + Math.round(Math.random() * 1000000);
    }

    @Override
    protected User createTestResource() {
        logger.debug("UserControllerTest : createTestResource");
        String userLogin = generateRandomLogin();
        String userPassword = "UserTestUserPassword";
        User u = new User();
        u.setLogin(userLogin);
        u.setPassword(userPassword);
        return u;
    }

    @Override
    protected String getResourcePath() {
        return "/api/user";
    }

    @Override
    protected User udpateTestResource(User u) {
        u.setLogin(generateRandomLogin());
        return u;
    }

    @Override
    protected Long getResourceId(User u) {
        return u.getId();
    }

    @Test
    public void shouldAddRoleToUser() throws Exception {
        // Given a new role
        Role r = new Role("Role" + Math.round(Math.random() * 100000));
        Response response = preparePost("/api/role").setBody(JsonHelper.serialize(r)).execute().get();
        r = JsonHelper.deserialize(response.getResponseBody(), Role.class);

        // Given a new user
        User u = this.createTestResource();
        response = preparePost("/api/user").setBody(JsonHelper.serialize(u)).execute().get();
        u = JsonHelper.deserialize(response.getResponseBody(), User.class);

        // When I associate the user and the role
        preparePut("/api/user/name/" + u.getLogin() + "/roles/" + r.getName()).execute().get();

        // Then I get the user with this role
        String userWithRole = prepareGet("/api/user/login/" + u.getLogin()).execute().get().getResponseBody();
        assertTrue("The user should contain the role", userWithRole.contains(r.getName()));
    }

    @Test
    public void shouldRemoveRoleFromUser() throws Exception {
        // Given a new role
        Role r = new Role("Role" + Math.round(Math.random() * 100000));
        Response response = preparePost("/api/role").setBody(JsonHelper.serialize(r)).execute().get();
        r = JsonHelper.deserialize(response.getResponseBody(), Role.class);

        // Given a new user
        User u = this.createTestResource();
        response = preparePost("/api/user").setBody(JsonHelper.serialize(u)).execute().get();
        u = JsonHelper.deserialize(response.getResponseBody(), User.class);
                
        preparePut("/api/user/name/" + u.getLogin() + "/roles/" + r.getName()).execute().get();
        prepareDelete("/api/user/name/" + u.getLogin() + "/roles/" + r.getName()).execute().get();

        // Then I get the user with this role
        String userWithRole = prepareGet("/api/user/login/" + u.getLogin()).execute().get().getResponseBody();
        assertFalse("The user shouldn't contain the role", userWithRole.contains(r.getName()));
    }

    @Test
    public void shouldGetRolesFromUsers() throws Exception {
        // Given some new roles
        Role r1 = new Role("role1");
        Role r2 = new Role("role2");
        Response response = preparePost("/api/role").setBody(JsonHelper.serialize(r1)).execute().get();
        r1 = JsonHelper.deserialize(response.getResponseBody(), Role.class);
        response = preparePost("/api/role").setBody(JsonHelper.serialize(r2)).execute().get();
        r2 = JsonHelper.deserialize(response.getResponseBody(), Role.class);
        
        // Given some new users
        User u1 = this.createTestResource();
        User u2 = this.createTestResource();
        User u3 = this.createTestResource();
        User u4 = this.createTestResource();
        response = preparePost("/api/user").setBody(JsonHelper.serialize(u1)).execute().get();
        u1 = JsonHelper.deserialize(response.getResponseBody(), User.class);
        response = preparePost("/api/user").setBody(JsonHelper.serialize(u2)).execute().get();
        u2 = JsonHelper.deserialize(response.getResponseBody(), User.class);
        response = preparePost("/api/user").setBody(JsonHelper.serialize(u3)).execute().get();
        u3 = JsonHelper.deserialize(response.getResponseBody(), User.class);
        response = preparePost("/api/user").setBody(JsonHelper.serialize(u4)).execute().get();
        u4 = JsonHelper.deserialize(response.getResponseBody(), User.class);
        
        // Given the association of the users with the roles
        // u1 with role1
        preparePut("/api/user/name/" + u1.getLogin() + "/roles/" + r1.getName()).execute().get();
        // u3 with role2
        preparePut("/api/user/name/" + u3.getLogin() + "/roles/" + r2.getName()).execute().get();
        // u4 with both role1 and role2
        preparePut("/api/user/name/" + u4.getLogin() + "/roles/" + r1.getName()).execute().get();
        preparePut("/api/user/name/" + u4.getLogin() + "/roles/" + r2.getName()).execute().get();

        // When I look for users with roles
        String user1Roles = prepareGet("/api/user/name/" + u1.getLogin() + "/roles").execute().get().getResponseBody();
        String user2Roles = prepareGet("/api/user/name/" + u2.getLogin() + "/roles").execute().get().getResponseBody();
        String user3Roles = prepareGet("/api/user/name/" + u3.getLogin() + "/roles").execute().get().getResponseBody();
        String user4Roles = prepareGet("/api/user/name/" + u4.getLogin() + "/roles").execute().get().getResponseBody();

        // Then the lists should only contain what I asked for
        assertTrue("The list of roles for user1 should contain role1", user1Roles.contains(r1.getName()));
        assertEquals("The list of roles for user2 should be empty", "[]", user2Roles);
        assertTrue("The list of roles for user3 should contain role2", user3Roles.contains(r2.getName()));
        assertTrue("The list of roles for user4 should contain role1 and role2", user4Roles.contains(r1.getName()) && user4Roles.contains(r2.getName()));
    }

    @Test
    public void cannotCreateTwiceTheSameUser() throws Exception {
        // Given a new user
        User u = this.createTestResource();

        // When I create it twice
        preparePost("/api/user").setBody(JsonHelper.serialize(u)).execute().get();
        Response response = preparePost("/api/user").setBody(JsonHelper.serialize(u)).execute().get();

        // Then the response should be a 409 error
        assertEquals(Http.CONFLICT, response.getStatusCode());
    }

    @Test
    public void shouldManageToCheckUserIdentity() throws IllegalArgumentException, InterruptedException, ExecutionException, IOException {
        // Given a created user
    	UserWithPassword u = new UserWithPassword(this.createTestResource());
        String password = u.getPassword();
        Response response = preparePost("/api/user").setBody(JsonHelper.serialize(u)).execute().get();
        User user = JsonHelper.deserialize(response.getResponseBody(), User.class);

        // When I check his identity
        Response postAnswerCorrectPass = preparePost("/api/user/checkuser?user=" + user.getLogin() + "&password=" + password).execute().get();
        Response postAnswerWrongPass = preparePost("/api/user/checkuser?user=" + user.getLogin() + "&password=wrongpassword").execute().get();

        assertEquals("The identity check should be successful", Http.NO_CONTENT, postAnswerCorrectPass.getStatusCode());
        assertEquals("The identity check should fail", Http.NOT_FOUND, postAnswerWrongPass.getStatusCode());
        
        response = prepareGet("/api/user").execute().get();
    }
}
