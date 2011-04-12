package org.resthub.identity.dao;

import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.core.test.dao.AbstractResourceDaoTest;
import org.resthub.identity.model.Role;
import static org.junit.Assert.*;

/**
 * Test class for <tt>RoleDao</tt>.
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public class RoleDaoTest extends AbstractResourceDaoTest<Role, RoleDao> {

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
    public void setResourceDao(RoleDao resourceDao) {
        super.setResourceDao(resourceDao);
    }

    @Override
    protected Role createTestRessource() throws Exception {
        Role testRole = new Role(generateRandomRoleName());
        return testRole;
    }

    @Override
    public void testUpdate() throws Exception {
        final String editedRoleName = generateRandomRoleName();

        Role role1 = this.resourceDao.readByPrimaryKey(this.getRessourceId());
        role1.setName(editedRoleName);
        resourceDao.save(role1);
        Role role2 = resourceDao.readByPrimaryKey(this.getRessourceId());
        assertEquals("Role not updated!", role2.getName(), editedRoleName);
    }
}
