package org.resthub.identity.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.web.JsonHelper;
import org.resthub.web.test.controller.AbstractControllerWebTest;

import com.ning.http.client.Response;

/**
 * Test class for <tt>RoleController</tt>.
 * 
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public class RoleControllerWebTest extends AbstractControllerWebTest<Role, Long> {

    protected Logger logger = Logger.getLogger(UserControllerWebTest.class);
    
    @Override
	public void setUp() throws Exception {
		this.useOpenEntityManagerInViewFilter = true;
		super.setUp();
	}

    /**
     * Generate a random role name based on a string and a randomized number.
     * 
     * @return A unique role name.
     */
    private String generateRandomRoleName() {
        return "RoleName" + Math.round(Math.random() * 10000000);
    }

    @Override
    protected String getResourcePath() {
        return "/api/role";
    }

    @Override
    protected Role udpateTestResource(Role r) {
        r.setName(generateRandomRoleName());
        return r;
    }

    @Override
    protected Long getResourceId(Role r) {
        return r.getId();
    }

    @Override
    protected Role createTestResource() {
        Role testRole = new Role(generateRandomRoleName());
        return testRole;
    }

    @Override
    @After
    public void tearDown() {
    	try {
			prepareDelete("/api/user/all").execute().get();
			prepareDelete(getResourcePath() + "/all").execute().get();
		} catch (InterruptedException | ExecutionException | IOException e) {
			e.printStackTrace();
		}
    	
        super.tearDown();
    }

    protected User createTestUser() {
        logger.debug("UserControllerTest : createTestUser");
        String userLogin = "UserTestUserLogin" + Math.round(Math.random() * 1000000);
        String userPassword = "UserTestUserPassword";
        User u = new User();
        u.setLogin(userLogin);
        u.setPassword(userPassword);
        return u;
    }

    @Test
    public void shouldGetUsersWithDirectRole() throws IOException, InterruptedException, ExecutionException {
        // Given some new roles
        Role r1 = new Role("role1");
        Role r2 = new Role("role2");
        
        Response response = preparePost("/api/role").setBody(JsonHelper.serialize(r1)).execute().get();
        r1 = JsonHelper.deserialize(response.getResponseBody(), Role.class); 
        
        response = preparePost("/api/role").setBody(JsonHelper.serialize(r2)).execute().get();
        r2 = JsonHelper.deserialize(response.getResponseBody(), Role.class);
        
        // Given some new users
        User u1 = this.createTestUser();
        User u2 = this.createTestUser();
        User u3 = this.createTestUser();
        User u4 = this.createTestUser();
        
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
        String notExistingRoleUsers = prepareGet("/api/role/inventedRole/users").execute().get().getResponseBody();
        String role1Users = prepareGet("/api/role/role1/users").execute().get().getResponseBody();
        String role2Users = prepareGet("/api/role/role2/users").execute().get().getResponseBody();

        // Then the lists should only contain what I asked for
        assertEquals("A search with an unknown role shouldn't bring anything", "[]", notExistingRoleUsers);

        assertTrue("The list of users with role1 should contain user1", role1Users.contains(u1.getLogin()));
        assertFalse("The list of users with role1 shouldn't contain user2", role1Users.contains(u2.getLogin()));
        assertFalse("The list of users with role1 shouldn't contain user3", role1Users.contains(u3.getLogin()));
        assertTrue("The list of users with role1 should contain user4", role1Users.contains(u4.getLogin()));

        assertFalse("The list of users with role2 shouldn't contain user1", role2Users.contains(u1.getLogin()));
        assertFalse("The list of users with role2 shouldn't contain user2", role2Users.contains(u2.getLogin()));
        assertTrue("The list of users with role2 should contain user3", role2Users.contains(u3.getLogin()));
        assertTrue("The list of users with role2 should contain user4", role2Users.contains(u4.getLogin()));
    }
}
