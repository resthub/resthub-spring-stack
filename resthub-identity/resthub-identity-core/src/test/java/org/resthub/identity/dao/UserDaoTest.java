package org.resthub.identity.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.resthub.core.test.dao.AbstractResourceDaoTest;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import static org.junit.Assert.*;

/**
 *
 * @author Guillaume Zurbach
 */
public class UserDaoTest extends AbstractResourceDaoTest<User, UserDao> {

    @Inject
    @Named("roleDao")
    protected RoleDao roleDao;
    @Inject
    @Named("groupDao")
    protected PermissionsOwnerDao<Group> groupDao;

    @Inject
    @Named("userDao")
    @Override
    public void setResourceDao(UserDao resourceDao) {
        super.setResourceDao(resourceDao);
    }

    public User createTestUser() {
        String userLogin = "UserTestUserLogin" + Math.round(Math.random() * 1000);
        String userPassword = "UserTestUserPassword";
        User u = new User();
        u.setLogin(userLogin);
        u.setPassword(userPassword);
        return u;
    }

    public Group createTestGroup() {
        Group g = new Group();
        g.setName("TestGroup" + Math.round(Math.random() * 1000));
        return g;
    }

    @SuppressWarnings("unchecked")
    @Before
    @Override
    public void setUp() throws Exception {
        User u = createTestUser();
        u = resourceDao.save(u);
        resourceId = u.getId();
    }

    @Override
    public void testUpdate() throws Exception {
        User u1 = resourceDao.readByPrimaryKey(this.getRessourceId());
        u1.setEmail("test@plop.fr");
        resourceDao.save(u1);
        User u2 = resourceDao.readByPrimaryKey(this.getRessourceId());
        assertEquals("User not updated!", u2.getEmail(), "test@plop.fr");
    }

    @Test
    @Override
    public void testSave() throws Exception {
        User u = createTestUser();
        u = resourceDao.save(u);

        User foundResource = resourceDao.readByPrimaryKey(u.getId());
        assertNotNull("Resource not found!", foundResource);
    }

    @Test
    public void testGetANDPermissions() {
        //given a new user
        User u1 = new User();
        String login = "alexDao";
        String password = "alexDao-pass";
        u1.setLogin(login);
        u1.setPassword(password);
        ArrayList<String> permissions = new ArrayList<String>();
        permissions.add("ADMIN");
        permissions.add("USER");
        u1.getPermissions().addAll(permissions);

        resourceDao.save(u1);

        //when we search  him by his login and password
        List<User> l = resourceDao.findEquals("Login", login);
        assertNotNull(l);

        u1 = l.get(0);
        //we get the user as response
        assertNotNull(u1);
        assertEquals(login, u1.getLogin());
        assertEquals(u1.getPermissions().get(0), "ADMIN");
    }

    @Test
    public void shouldGetUsersWithDirectRole() {
        // Given some new roles
        Role r1 = new Role("role1");
        Role r2 = new Role("role2");
        r1 = roleDao.save(r1);
        r2 = roleDao.save(r2);

        // Given some new users
        // u1 with role1
        User u1 = this.createTestUser();
        u1.getRoles().add(r1);
        // u2 without any role
        User u2 = this.createTestUser();
        // u3 with role2
        User u3 = this.createTestUser();
        u3.getRoles().add(r2);
        // u4 with both role1 and role2
        User u4 = this.createTestUser();
        u4.getRoles().add(r1);
        u4.getRoles().add(r2);

        u1 = this.resourceDao.save(u1);
        u2 = this.resourceDao.save(u2);
        u3 = this.resourceDao.save(u3);
        u4 = this.resourceDao.save(u4);

        // When I look for users with roles
        List<User> notExistingRoleUsers = this.resourceDao.findAllUsersWithRoles(Arrays.asList("role"));
        List<User> role1Users = this.resourceDao.findAllUsersWithRoles(Arrays.asList("role1"));
        List<User> role2Users = this.resourceDao.findAllUsersWithRoles(Arrays.asList("role2"));
        List<User> role1AndRole2Users = this.resourceDao.findAllUsersWithRoles(Arrays.asList("role1", "role2"));

        // Then the lists should only contain what I asked for
        assertTrue("A search with an unknown role shouldn't bring anything", notExistingRoleUsers.isEmpty());

        assertEquals("The list of users with role1 should contain 2 elements", 2, role1Users.size());
        assertTrue("The list of users with role1 should contain user1", role1Users.contains(u1));
        assertTrue("The list of users with role1 should contain user4", role1Users.contains(u4));

        assertEquals("The list of users with role2 should contain 2 elements", 2, role2Users.size());
        assertTrue("The list of users with role2 should contain user3", role2Users.contains(u3));
        assertTrue("The list of users with role2 should contain user4", role2Users.contains(u4));

        assertEquals("The list of users with role1 and role2 should contain 3 elements", 3, role1AndRole2Users.size());
        assertTrue("The list of users with role2 should contain user1", role1AndRole2Users.contains(u1));
        assertTrue("The list of users with role2 should contain user3", role1AndRole2Users.contains(u3));
        assertTrue("The list of users with role2 should contain user4", role1AndRole2Users.contains(u4));
    }

    /**
     * Here is a little scheme of the hierarchy that will be set in this test
     * g1 (r1)
     * |_g2 (r2)
     * | |_g4 (r4)
     * |   |_u1 (r1)
     * |   |_u3
     * |_g3 (r3)
     *   |_u2
     *   |_u3
     *   |_u4 (r4)
     */
    @Test
    public void shouldGetUsersWithInheritedRoles() {
        // Given some new roles
        Role r1 = new Role("role1");
        Role r2 = new Role("role2");
        Role r3 = new Role("role3");
        Role r4 = new Role("role4");
        r1 = roleDao.save(r1);
        r2 = roleDao.save(r2);
        r3 = roleDao.save(r3);
        r4 = roleDao.save(r4);

        // Given some new groups
        Group g1 = this.createTestGroup();
        Group g2 = this.createTestGroup();
        Group g3 = this.createTestGroup();
        Group g4 = this.createTestGroup();

        g1.getRoles().add(r1); // add role1 to g1
        g2.getGroups().add(g1); // add g1 as parent of g2
        g2.getRoles().add(r2); // add role2 to g2
        g3.getGroups().add(g1); // add g1 as parent of g3
        g3.getRoles().add(r3); // add role3 to g3
        g4.getGroups().add(g2); // add g2 as parent of g4
        g4.getRoles().add(r4); // add role4 to g4

        g1 = this.groupDao.save(g1);
        g2 = this.groupDao.save(g2);
        g3 = this.groupDao.save(g3);
        g4 = this.groupDao.save(g4);

        // Given some new users
        // u1 with direct role1 and inside group4
        User u1 = this.createTestUser();
        u1.getRoles().add(r1); // add role1 to u1
        u1.getGroups().add(g4); // add group4 as parent of u1

        // u2 without any role and inside group3
        User u2 = this.createTestUser();
        u2.getGroups().add(g3);

        // u3 without any role and inside group3 and group4
        User u3 = this.createTestUser();
        u3.getGroups().add(g3);
        u3.getGroups().add(g4);

        // u4 with role4 and inside group3
        User u4 = this.createTestUser();
        u4.getRoles().add(r4);
        u4.getGroups().add(g3);

        u1 = this.resourceDao.save(u1);
        u2 = this.resourceDao.save(u2);
        u3 = this.resourceDao.save(u3);
        u4 = this.resourceDao.save(u4);

        // When I look for users with roles
        List<User> notExistingRoleUsers = this.resourceDao.findAllUsersWithRoles(Arrays.asList("role"));
        List<User> role1Users = this.resourceDao.findAllUsersWithRoles(Arrays.asList("role1"));
        List<User> role2Users = this.resourceDao.findAllUsersWithRoles(Arrays.asList("role2"));
        List<User> role3Users = this.resourceDao.findAllUsersWithRoles(Arrays.asList("role3"));
        List<User> role4Users = this.resourceDao.findAllUsersWithRoles(Arrays.asList("role4"));
        List<User> role2AndRole3Users = this.resourceDao.findAllUsersWithRoles(Arrays.asList("role2", "role3"));

        // Then the lists should only contain what I asked for
        assertTrue("A search with an unknown role shouldn't bring anything", notExistingRoleUsers.isEmpty());

        assertEquals("The list of users with role1 should contain 4 elements", 4, role1Users.size());
        assertTrue("The list of users with role1 should contain user1", role1Users.contains(u1));
        assertTrue("The list of users with role1 should contain user2", role1Users.contains(u2));
        assertTrue("The list of users with role1 should contain user3", role1Users.contains(u3));
        assertTrue("The list of users with role1 should contain user4", role1Users.contains(u4));

        assertEquals("The list of users with role2 should contain 2 elements", 2, role2Users.size());
        assertTrue("The list of users with role2 should contain user1", role2Users.contains(u1));
        assertTrue("The list of users with role2 should contain user3", role2Users.contains(u3));

        assertEquals("The list of users with role3 should contain 3 elements", 3, role3Users.size());
        assertTrue("The list of users with role3 should contain user2", role3Users.contains(u2));
        assertTrue("The list of users with role3 should contain user3", role3Users.contains(u3));
        assertTrue("The list of users with role3 should contain user4", role3Users.contains(u4));

        assertEquals("The list of users with role4 should contain 3 elements", 3, role4Users.size());
        assertTrue("The list of users with role4 should contain user1", role4Users.contains(u1));
        assertTrue("The list of users with role4 should contain user3", role4Users.contains(u3));
        assertTrue("The list of users with role4 should contain user4", role4Users.contains(u4));

        assertEquals("The list of users with role2 and role3 should contain 4 elements", 4, role2AndRole3Users.size());
        assertTrue("The list of users with role2 and role3 should contain user1", role2AndRole3Users.contains(u1));
        assertTrue("The list of users with role2 and role3 should contain user2", role2AndRole3Users.contains(u2));
        assertTrue("The list of users with role2 and role3 should contain user3", role2AndRole3Users.contains(u3));
        assertTrue("The list of users with role2 and role3 should contain user4", role2AndRole3Users.contains(u4));
    }
}
