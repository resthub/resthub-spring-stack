package org.resthub.identity.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.model.UserWithPassword;
import org.resthub.web.test.controller.AbstractControllerWebTest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

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
        r = resource().path("/api/role").type(MediaType.APPLICATION_JSON).post(Role.class, r);

        // Given a new user
        User u = this.createTestResource();
        u = resource().path("/api/user").type(MediaType.APPLICATION_JSON).post(User.class, u);

        // When I associate the user and the role
        resource().path("/api/user/name/" + u.getLogin() + "/roles/" + r.getName()).type(MediaType.APPLICATION_JSON).put();

        // Then I get the user with this role
        String userWithRole = resource().path("/api/user/login/" + u.getLogin()).accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        assertTrue("The user should contain the role", userWithRole.contains(r.getName()));
    }

    @Test
    public void shouldRemoveRoleFromUser() throws Exception {
        // Given a new role
        Role r = new Role("Role" + Math.round(Math.random() * 100000));
        r = resource().path("/api/role").type(MediaType.APPLICATION_JSON).post(Role.class, r);

        // Given a new user
        User u = this.createTestResource();
        u = resource().path("/api/user").type(MediaType.APPLICATION_JSON).post(User.class, u);
        resource().path("/api/user/name/" + u.getLogin() + "/roles/" + r.getName()).type(MediaType.APPLICATION_JSON).put();

        // When I associate the user and the role
        resource().path("/api/user/name/" + u.getLogin() + "/roles/" + r.getName()).type(MediaType.APPLICATION_JSON).delete();

        // Then I get the user with this role
        String userWithRole = resource().path("/api/user/login/" + u.getLogin()).accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        assertFalse("The user shouldn't contain the role", userWithRole.contains(r.getName()));
    }

    @Test
    public void shouldGetRolesFromUsers() throws Exception {
        // Given some new roles
        Role r1 = new Role("role1");
        Role r2 = new Role("role2");
        r1 = resource().path("/api/role").type(MediaType.APPLICATION_JSON).post(Role.class, r1);
        r2 = resource().path("/api/role").type(MediaType.APPLICATION_JSON).post(Role.class, r2);

        // Given some new users
        User u1 = this.createTestResource();
        User u2 = this.createTestResource();
        User u3 = this.createTestResource();
        User u4 = this.createTestResource();
        u1 = resource().path("/api/user").type(MediaType.APPLICATION_JSON).post(User.class, u1);
        u2 = resource().path("/api/user").type(MediaType.APPLICATION_JSON).post(User.class, u2);
        u3 = resource().path("/api/user").type(MediaType.APPLICATION_JSON).post(User.class, u3);
        u4 = resource().path("/api/user").type(MediaType.APPLICATION_JSON).post(User.class, u4);

        // Given the association of the users with the roles
        // u1 with role1
        resource().path("/api/user/name/" + u1.getLogin() + "/roles/" + r1.getName()).type(MediaType.APPLICATION_JSON).put();
        // u3 with role2
        resource().path("/api/user/name/" + u3.getLogin() + "/roles/" + r2.getName()).type(MediaType.APPLICATION_JSON).put();
        // u4 with both role1 and role2
        resource().path("/api/user/name/" + u4.getLogin() + "/roles/" + r1.getName()).type(MediaType.APPLICATION_JSON).put();
        resource().path("/api/user/name/" + u4.getLogin() + "/roles/" + r2.getName()).type(MediaType.APPLICATION_JSON).put();

        // When I look for users with roles
        String user1Roles = resource().path("/api/user/name/" + u1.getLogin() + "/roles").accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        String user2Roles = resource().path("/api/user/name/" + u2.getLogin() + "/roles").accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        String user3Roles = resource().path("/api/user/name/" + u3.getLogin() + "/roles").accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        String user4Roles = resource().path("/api/user/name/" + u4.getLogin() + "/roles").accept(MediaType.APPLICATION_JSON)
                .get(String.class);

        // Then the lists should only contain what I asked for
        assertTrue("The list of roles for user1 should contain role1", user1Roles.contains(r1.getName()));
        assertEquals("The list of roles for user2 should be empty", "[]", user2Roles);
        assertTrue("The list of roles for user3 should contain role2", user3Roles.contains(r2.getName()));
        assertTrue("The list of roles for user4 should contain role1 and role2", user4Roles.contains(r1.getName())
                && user4Roles.contains(r2.getName()));
    }

    @Test
    public void cannotCreateTwiceTheSameUser() throws Exception {
        // Given a new user
        User u = this.createTestResource();

        // When I create it twice
        resource().path("/api/user").type(MediaType.APPLICATION_JSON).post(u);
        ClientResponse response = resource().path("/api/user").type(MediaType.APPLICATION_JSON).post(ClientResponse.class, u);

        // Then the response should be a 409 error
        assertEquals(Status.CONFLICT, response.getClientResponseStatus());
    }

    @Test
    public void shouldManageToCheckUserIdentity() {
        // Given a created user
    	UserWithPassword u = new UserWithPassword(this.createTestResource());
        String password = u.getPassword();
        User user = resource().path("/api/user").type(MediaType.APPLICATION_JSON).post(User.class, u);

        // When I check his identity
        ClientResponse postAnswerCorrectPass = resource().path("/api/user/checkuser").queryParam("user", user.getLogin())
                .queryParam("password", password).post(ClientResponse.class);
        ClientResponse postAnswerWrongPass = resource().path("/api/user/checkuser").queryParam("user", user.getLogin())
                .queryParam("password", "wrongpassword").post(ClientResponse.class);

        assertEquals("The identity check should be successful", Status.NO_CONTENT,
                postAnswerCorrectPass.getClientResponseStatus());
        assertEquals("The identity check should fail", Status.NOT_FOUND, postAnswerWrongPass.getClientResponseStatus());
        
        String response = resource().path("/api/user").accept(MediaType.APPLICATION_JSON).get(String.class);
    }
}
