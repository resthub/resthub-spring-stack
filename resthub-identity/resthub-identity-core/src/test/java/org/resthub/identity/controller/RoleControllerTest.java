package org.resthub.identity.controller;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;
import org.resthub.identity.model.Role;
import org.resthub.identity.service.RoleService;
import org.resthub.web.test.controller.AbstractResourceControllerTest;
import static org.junit.Assert.*;

/**
 * Test class for <tt>RoleController</tt>.
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public class RoleControllerTest extends AbstractResourceControllerTest<Role, RoleService, RoleController> {

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

    @Override
    public void testUpdate() throws Exception {
        // Given a new role
        Role testRole = this.createTestResource();
        final String initialRoleName = testRole.getName();
        testRole =  resource().path("role").type(MediaType.APPLICATION_XML).post(Role.class, testRole);

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
}
