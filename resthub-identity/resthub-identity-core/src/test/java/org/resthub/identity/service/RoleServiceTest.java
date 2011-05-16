package org.resthub.identity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.service.AbstractServiceTest;
import org.resthub.identity.model.Role;

/**
 * Test class for <tt>RoleService</tt>.
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public class RoleServiceTest extends AbstractServiceTest<Role, Long, RoleService> {

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
    public void setService(RoleService service) {
        super.setService(service);
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
        testRole = this.service.create(testRole);

        // When I update this role
        final String newRoleName = this.generateRandomRoleName();
        testRole.setName(newRoleName);
        testRole = this.service.update(testRole);

        // Then the modification is done.
        assertEquals("Role not updated!", testRole.getName(), newRoleName);
    }

    @Test
    public void shouldFindByName() {
        // Given a new role
        Role r = this.createTestRessource();
        r = this.service.create(r);

        // When I find it by name
        Role roleFromName = this.service.findByName(r.getName());

        // Then I can find it
        assertNotNull("The role should be found", roleFromName);
        assertEquals("The role found should be the same as the one created", r, roleFromName);
    }

    @Test
    public void shouldNotFindRoleWithWeirdName() {
        // Given a new role
        Role r = this.createTestRessource();
        r = this.service.create(r);

        // When I find it with a weird name
        Role roleFromName = this.service.findByName("InventedNameThatShouldntBringAnyResult");

        // Then I can find it
        assertNull("No role should be found", roleFromName);
    }

    @Test
    public void shouldFindNameWithWildcard() {
        // Given a new role
        Role r = this.createTestRessource();
        r = this.service.create(r);

        // When I search for a part of its name
        List<Role> roles = this.service.findByNameLike(r.getName().substring(0, 9) + "%");

        // Then the list is not empty and contains our role
        assertFalse("The list of roles shouldn't be empty", roles.isEmpty());
        assertTrue("The list of roles should contain our role", roles.contains(r));
    }
}
