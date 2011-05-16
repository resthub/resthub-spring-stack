package org.resthub.identity.dao;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.test.dao.AbstractDaoTest;
import org.resthub.identity.model.Role;

/**
 * Test class for <tt>RoleDao</tt>.
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public class RoleDaoTest extends AbstractDaoTest<Role, Long, RoleDao> {

    /**
     * Generate a random role name based on a string and a randomized number.
     * @return A unique role name.
     */
    private String generateRandomRoleName() {
        return "RoleName" + Math.round(Math.random() * 1000);
    }

    @Inject
    @Named("roleDao")
    @Override
    public void setDao(RoleDao resourceDao) {
        super.setDao(resourceDao);
    }

    @Override
    protected Role createTestEntity() throws Exception {
        Role testRole = new Role(generateRandomRoleName());
        return testRole;
    }

    @Override
    public void testUpdate() throws Exception {
        final String editedRoleName = generateRandomRoleName();

        Role role1 = this.dao.readByPrimaryKey(this.id);
        role1.setName(editedRoleName);
        dao.save(role1);
        Role role2 = dao.readByPrimaryKey(this.id);
        assertEquals("Role not updated!", role2.getName(), editedRoleName);
    }
}
