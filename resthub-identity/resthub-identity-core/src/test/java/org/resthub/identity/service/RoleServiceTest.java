package org.resthub.identity.service;

import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.core.test.service.AbstractResourceServiceTest;
import org.resthub.identity.model.Role;
import static org.junit.Assert.*;

/**
 * Test class for <tt>RoleService</tt>.
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public class RoleServiceTest extends AbstractResourceServiceTest<Role, RoleService> {

    /**
     * Generate a random role name based on a string and a randomized number.
     * @return A unique role name.
     */
    private String generateRandomRoleName() {
        return "RoleName" + Math.round(Math.random() * 1000);
    }

    @Inject
    @Named("roleService")
    @Override
    public void setResourceService(RoleService resourceService) {
        super.setResourceService(resourceService);
    }

    @Override
    public Role createTestRessource() {
        Role testRole = new Role(generateRandomRoleName());
        return testRole;
    }

    @Override
    public void testUpdate() throws Exception {
        // Given a new role
        Role testRole = this.createTestRessource();
        testRole = this.resourceService.create(testRole);

        // When I update this role
        final String newRoleName = this.generateRandomRoleName();
        testRole.setName(newRoleName);
        testRole = this.resourceService.update(testRole);

        // Then the modification is done.
        assertEquals("Role not updated!", testRole.getName(), newRoleName);
    }
}
