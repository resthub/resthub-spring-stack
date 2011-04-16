package org.resthub.identity.controller;

import org.resthub.identity.model.User;
import org.junit.Test;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;
import org.resthub.identity.model.Role;
import org.resthub.identity.service.RoleService;
import org.resthub.web.test.controller.AbstractResourceControllerTest;
import static org.junit.Assert.*;
import org.apache.log4j.Logger;

/**
 * Test class for <tt>RoleController</tt>.
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public class RoleControllerTest extends AbstractResourceControllerTest<Role, RoleService, RoleController> {

    protected Logger logger = Logger.getLogger(UserControllerTest.class);

    /**
     * Generate a random role name based on a string and a randomized number.
     * @return A unique role name.
     */
    private String generateRandomRoleName() {
        return "RoleName" + Math.round(Math.random() * 1000);
    }

    @Override
    @Inject
    @Named("roleController")
    public void setController(RoleController controller) {
        super.setController(controller);
    }

    @Override
    protected Role createTestResource() throws Exception {
        Role testRole = new Role(generateRandomRoleName());
        return testRole;
    }

    protected User createTestUser() {
        logger.debug("UserControllerTest : createTestUser");
        String userLogin = "UserTestUserLogin" + Math.round(Math.random() * 1000);
        String userPassword = "UserTestUserPassword";
        User u = new User();
        u.setLogin(userLogin);
        u.setPassword(userPassword);
        return u;
    }

    @Override
    public void testUpdate() throws Exception {
        // Given a new role
        Role testRole = this.createTestResource();
        final String initialRoleName = testRole.getName();
        testRole = resource().path("role").type(MediaType.APPLICATION_XML).post(Role.class, testRole);

        // When I update this role
        final String newRoleName = this.generateRandomRoleName();
        testRole.setName(newRoleName);
        ClientResponse cr = resource().path("role/" + testRole.getId()).type(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, testRole);

        // Then the modification is done.
        assertEquals("Role not updated!", Status.CREATED.getStatusCode(), cr.getStatus());
        String response = resource().path("role").accept(MediaType.APPLICATION_JSON).get(String.class);
        assertFalse("Role not updated!", response.contains(initialRoleName));
        assertTrue("Role not updated!", response.contains(newRoleName));
    }

    @Test
    public void shouldGetUsersWithDirectRole() {
        // Given some new roles
        Role r1 = new Role("role1");
        Role r2 = new Role("role2");
        r1 = resource().path("role").type(MediaType.APPLICATION_XML).post(Role.class, r1);
        r2 = resource().path("role").type(MediaType.APPLICATION_XML).post(Role.class, r2);

        // Given some new users
        User u1 = this.createTestUser();
        User u2 = this.createTestUser();
        User u3 = this.createTestUser();
        User u4 = this.createTestUser();
        u1 = resource().path("user").type(MediaType.APPLICATION_XML).post(User.class, u1);
        u2 = resource().path("user").type(MediaType.APPLICATION_XML).post(User.class, u2);
        u3 = resource().path("user").type(MediaType.APPLICATION_XML).post(User.class, u3);
        u4 = resource().path("user").type(MediaType.APPLICATION_XML).post(User.class, u4);

        // Given the association of the users with the roles
        // u1 with role1
        resource().path("user/name/" + u1.getLogin() + "/roles/" + r1.getName()).type(MediaType.APPLICATION_XML).put();
        // u3 with role2
        resource().path("user/name/" + u3.getLogin() + "/roles/" + r2.getName()).type(MediaType.APPLICATION_XML).put();
        // u4 with both role1 and role2
        resource().path("user/name/" + u4.getLogin() + "/roles/" + r1.getName()).type(MediaType.APPLICATION_XML).put();
        resource().path("user/name/" + u4.getLogin() + "/roles/" + r2.getName()).type(MediaType.APPLICATION_XML).put();

        // When I look for users with roles
        String notExistingRoleUsers = resource().path("role/inventedRole/users").accept(MediaType.APPLICATION_JSON).get(String.class);
        String role1Users = resource().path("role/role1/users").accept(MediaType.APPLICATION_JSON).get(String.class);
        String role2Users = resource().path("role/role2/users").accept(MediaType.APPLICATION_JSON).get(String.class);

        // Then the lists should only contain what I asked for
        assertEquals("A search with an unknown role shouldn't bring anything", "[ ]", notExistingRoleUsers);

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
