package org.resthub.identity.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.core.test.service.AbstractServiceTest;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.service.UserService.UserServiceChange;

public class UserServiceTest extends AbstractServiceTest<User, Long, UserService> {

    @Inject
    @Named("userService")
    @Override
    public void setService(UserService service) {
        super.setService(service);
    }
    @Inject
    @Named("groupService")
    private GroupService groupService;
    @Inject
    @Named("roleService")
    private RoleService roleService;

    @Override
    public User createTestRessource() {
        String userLogin = "UserTestUserName" + Math.round(Math.random() * 100000);
        String userPassword = "UserTestUserPassword";
        User u = new User();
        u.setLogin(userLogin);
        u.setPassword(userPassword);
        return u;
    }

    public Group createTestGroup() {
        Group g = new Group();
        g.setName("TestGroup" + Math.round(Math.random() * 100000));
        return g;
    }
    /*
     * The UserService is needed because we have specific method for password
     * management
     */
    UserService userService;

    @Inject
    @Named("userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void testMultiDeletion() throws Exception {

        User u1 = new User();
        u1.setLogin("u1");

        User u2 = new User();
        u2.setLogin("u2");

        u1 = service.create(u1);
        u2 = service.create(u2);

        service.delete(u1);
        service.delete(u2);

        Assert.assertNull(service.findById(u1.getId()));
        Assert.assertNull(service.findById(u2.getId()));

    }

    @Override
    @Test
    public void testUpdate() throws Exception {
        /* Given a new user */
        String firstName = "alexander";
        String firstNameAbr = "alex";
        String lastName = "testUpdate";
        String password = "testPassword";
        String login = "testLogin";

        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setPassword(password);
        u.setLogin(login);
        u = this.service.create(u);

        // when we try to change some info (firstName) about the user and that we give the good password
        Long uid = u.getId();
        u = new User(u);
        u.setId(uid);
        u.setFirstName(firstNameAbr);
        u.setPassword(password);
        u = service.update(u);

        // Then The modification is updated
        assertEquals(u.getFirstName(), firstNameAbr);

        this.service.delete(u);
    }

    @Test
    public void shouldGetUserByAuthenticationInformationWhenOK() {
        /* Given a new user */

        String login = "alexOK";
        String password = "alex-pass";
        User u = new User();
        u.setLogin(login);
        u.setPassword(password);
        service.create(u);
        // userService.create(u);

        /* When we search him with good login and password */
        // u = userService.authenticateUser(login, password);

        u = service.authenticateUser(login, password);

        /* Then we retrieve the good user */
        assertNotNull(u);
        assertEquals(u.getLogin(), login);
        service.delete(u);
        // userService.delete(u);
    }

    @Test
    public void shouldGetUserByAuthenticationInformationWhenOKAfterUpdate() {
        /* Given a new user */

        String login = "alexOK";
        String password1 = "alex-pass";
        String password2 = "NewAlex-pass";

        User u = new User();
        u.setLogin(login);
        u.setPassword(password1);
        service.create(u);

        /* After that the password is updates */
        u.setPassword(password2);
        u = service.updatePassword(u);

        /* When we search him with good login and password */
        u = service.authenticateUser(login, password2);

        /* Then we retrieve the good user */
        assertNotNull(u);
        assertEquals(u.getLogin(), login);

        service.delete(u);
        // userService.delete(u);
    }

    @Test
    public void shouldGetNullWhenBadPassword() {
        /* Given a new user */
        String login = "alexBadPassword";
        String password = "alex-pass";
        String badPassword = "alex-bad-pass";

        User u = new User();
        u.setLogin(login);
        u.setPassword(password);
        service.create(u);
        // userService.create(u);
		/* When we search him providing a bad password */
        User retrievedUser = service.authenticateUser(login,
                badPassword);

        /* Then the user is not retrieved */
        assertNull(retrievedUser);

        service.delete(u);
    }

    @Test
    public void shouldGetNullWhenBadLogin() {
        String login = "alexBadLogin";
        String badLogin = "alex";
        String password = "alex-password";
        /* Given a new user */

        User u = new User();
        u.setLogin(login);
        u.setPassword(password);
        service.create(u);

        /* When we search him providing a bad login */
        User retrievedUser = service.authenticateUser(badLogin,
                password);
        /* Then the user is not retrieved */

        assertNull(retrievedUser);

        this.service.delete(u);
    }

    @Test
    public void testDirectPermissions() {
        /* Given a user with permissions */
        String login = "permissionLogin";
        String password = "Password";

        // direct permissions
        List<String> permissions = new ArrayList<String>();
        permissions.add("ADMIN");
        permissions.add("USER");

        User u = new User();
        u.setLogin(login);
        u.setPassword(password);
        u.getPermissions().addAll(permissions);
        service.create(u);

        /* When we retrieved him after a search */
        u = service.findByLogin(login);

        /* We can get the direct permissions */
        assertEquals("Permissions not found", 2, u.getPermissions().size());
        assertTrue("Permissions not found", u.getPermissions().contains("ADMIN"));
        assertTrue("Permissions not found", u.getPermissions().contains("USER"));

        service.delete(u);

    }

    @Test
    public void testGroupsPermissions() {
        /* Given a user with permissions */
        String login = "permissionLogin";
        String password = "Password";

        // direct permissions
        List<String> permissions = new ArrayList<String>();
        permissions.add("ADMIN");
        permissions.add("USER");

        // a group and a subGroup
        Group group = new Group();
        Group subGroup = new Group();
        group.setName("TestGroup");
        subGroup.setName("TestSubGroup");

        // add a permission to each group
        group.getPermissions().add("TESTGROUPPERMISSION");
        subGroup.getPermissions().add("TESTSUBGROUPPERMISSION");

        // make subGroup a permission of group
        group.getGroups().add(subGroup);

        subGroup = groupService.create(subGroup);
        group = groupService.create(group);

        User u = new User();
        u.setLogin(login);
        u.setPassword(password);
        u.getPermissions().addAll(permissions);
        u.getGroups().add(group);
        service.create(u);

        /* When we retrieved him after a search */
        u = service.findByLogin(login);

        /* We can get the direct permissions */
        assertEquals("Permissions not found", 2, u.getPermissions().size());
        assertTrue("Permissions not found", u.getPermissions().contains("ADMIN"));
        assertTrue("Permissions not found", u.getPermissions().contains("USER"));

        /* now with the permissions from groups */
        List<String> allPermissions = service.getUserPermissions(login);
        assertEquals("Permissions not found", 4, allPermissions.size());
        assertTrue("Permissions not found", allPermissions.contains("ADMIN"));
        assertTrue("Permissions not found", allPermissions.contains("USER"));
        assertTrue("Permissions not found", allPermissions.contains("TESTGROUPPERMISSION"));
        assertTrue("Permissions not found", allPermissions.contains("TESTSUBGROUPPERMISSION"));

        // TODO : remove this when we will use DBunit
        service.delete(u);
        groupService.delete(group);
        groupService.delete(subGroup);
    }

    @Test
    public void testDuplicatePermissions() {
        /* Given a user with permissions */
        String login = "permissionLogin";
        String password = "Password";

        // direct permissions
        List<String> permissions = new ArrayList<String>();
        permissions.add("ADMIN");
        permissions.add("USER");

        // a group and a subGroup
        Group group = new Group();
        Group subGroup = new Group();
        group.setName("TestGroup");
        subGroup.setName("TestSubGroup");

        // add a permission to each group
        group.getPermissions().add("TESTGROUPPERMISSION");
        subGroup.getPermissions().add("TESTSUBGROUPPERMISSION");

        // this is the test: USER is already a direct permission of this user
        // getUserPermission should return only one time the permission USER
        group.getPermissions().add("USER");

        // make subGroup a permission of group
        group.getGroups().add(subGroup);

        subGroup = groupService.create(subGroup);
        group = groupService.create(group);

        User u = new User();
        u.setLogin(login);
        u.setPassword(password);
        u.getPermissions().addAll(permissions);
        u.getGroups().add(group);
        service.create(u);

        /* When we retrieved him after a search */
        u = service.findByLogin(login);

        /* We can get the direct permissions */
        assertEquals("Permissions not found", 2, u.getPermissions().size());
        assertTrue("Permissions not found", u.getPermissions().contains("ADMIN"));
        assertTrue("Permissions not found", u.getPermissions().contains("USER"));

        /* now with the permissions from groups */
        List<String> allPermissions = service.getUserPermissions(login);
        assertEquals("Permissions not found", 4, allPermissions.size());
        assertTrue("Permissions not found", allPermissions.contains("ADMIN"));
        // the USER permission should exists only once in the list
        assertTrue("Permissions not found", allPermissions.contains("USER"));
        assertTrue("Permissions not found", allPermissions.contains("TESTGROUPPERMISSION"));
        assertTrue("Permissions not found", allPermissions.contains("TESTSUBGROUPPERMISSION"));

        // TODO : remove this when we will use DBunit
        service.delete(u);
        groupService.delete(group);
        groupService.delete(subGroup);
    }

    @Test
    public void testRolesPermissions() {
        // TODO: when roles are implemented, test that getUserPermissions also retrieve
        // the permissions set from roles.
    }

    @Test
    public void testShouldGetUsersFromGroup() {
        /* Given a new group */
        String groupName = "testGroup";
        Group g = new Group();
        g.setName(groupName);
        this.groupService.create(g);

        /* Given a new user */
        String firstName = "first";
        String lastName = "last";
        String password = "pass";
        String login = "testLogin";

        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setPassword(password);
        u.setLogin(login);

        /* Given a link between this user and the group */
        u.getGroups().add(g);
        u = this.service.create(u);

        /* When I get the users of the group */
        List<User> usersFromGroup = this.service.getUsersFromGroup(groupName);

        /* Then the list of users contains our user */
        assertTrue("The list of users should contain our just added user", usersFromGroup.contains(u));

        /* Cleanup */
        this.service.delete(u);
        this.groupService.delete(g);
    }

    @Test
    public void shouldGetUsersWithDirectRole() {
        // Given some new roles
        Role r1 = new Role("role1");
        Role r2 = new Role("role2");
        r1 = this.roleService.create(r1);
        r2 = this.roleService.create(r2);

        // Given some new users
        // u1 with role1
        User u1 = this.createTestRessource();
        u1.getRoles().add(r1);
        // u2 without any role
        User u2 = this.createTestRessource();
        // u3 with role2
        User u3 = this.createTestRessource();
        u3.getRoles().add(r2);
        // u4 with both role1 and role2
        User u4 = this.createTestRessource();
        u4.getRoles().add(r1);
        u4.getRoles().add(r2);

        u1 = this.service.create(u1);
        u2 = this.service.create(u2);
        u3 = this.service.create(u3);
        u4 = this.service.create(u4);

        // When I look for users with roles
        List<User> notExistingRoleUsers = this.service.findAllUsersWithRoles(Arrays.asList("role"));
        List<User> role1Users = this.service.findAllUsersWithRoles(Arrays.asList("role1"));
        List<User> role2Users = this.service.findAllUsersWithRoles(Arrays.asList("role2"));
        List<User> role1AndRole2Users = this.service.findAllUsersWithRoles(Arrays.asList("role1", "role2"));

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

        // TODO : remove this when we will use DBunit
        u1.getRoles().clear();
        this.service.update(u1);
        u2.getRoles().clear();
        this.service.update(u2);
        u3.getRoles().clear();
        this.service.update(u3);
        u4.getRoles().clear();
        this.service.update(u4);
        this.roleService.deleteAll();
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
        r1 = this.roleService.create(r1);
        r2 = this.roleService.create(r2);
        r3 = this.roleService.create(r3);
        r4 = this.roleService.create(r4);

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

        g1 = this.groupService.create(g1);
        g2 = this.groupService.create(g2);
        g3 = this.groupService.create(g3);
        g4 = this.groupService.create(g4);

        // Given some new users
        // u1 with direct role1 and inside group4
        User u1 = this.createTestRessource();
        u1.getRoles().add(r1); // add role1 to u1
        u1.getGroups().add(g4); // add group4 as parent of u1

        // u2 without any role and inside group3
        User u2 = this.createTestRessource();
        u2.getGroups().add(g3);

        // u3 without any role and inside group3 and group4
        User u3 = this.createTestRessource();
        u3.getGroups().add(g3);
        u3.getGroups().add(g4);

        // u4 with role4 and inside group3
        User u4 = this.createTestRessource();
        u4.getRoles().add(r4);
        u4.getGroups().add(g3);

        u1 = this.service.create(u1);
        u2 = this.service.create(u2);
        u3 = this.service.create(u3);
        u4 = this.service.create(u4);

        // When I look for users with roles
        List<User> notExistingRoleUsers = this.service.findAllUsersWithRoles(Arrays.asList("role"));
        List<User> role1Users = this.service.findAllUsersWithRoles(Arrays.asList("role1"));
        List<User> role2Users = this.service.findAllUsersWithRoles(Arrays.asList("role2"));
        List<User> role3Users = this.service.findAllUsersWithRoles(Arrays.asList("role3"));
        List<User> role4Users = this.service.findAllUsersWithRoles(Arrays.asList("role4"));
        List<User> role2AndRole3Users = this.service.findAllUsersWithRoles(Arrays.asList("role2", "role3"));

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

        // TODO : remove this when we will use DBunit
        u1.getRoles().clear();
        u1.getGroups().clear();
        this.service.update(u1);
        u2.getRoles().clear();
        u2.getGroups().clear();
        this.service.update(u2);
        u3.getRoles().clear();
        u3.getGroups().clear();
        this.service.update(u3);
        u4.getRoles().clear();
        u4.getGroups().clear();
        this.service.update(u4);

        g1.getRoles().clear();
        this.groupService.update(g1);
        g2.getRoles().clear();
        this.groupService.update(g2);
        g3.getRoles().clear();
        this.groupService.update(g3);
        g4.getRoles().clear();
        this.groupService.update(g4);

        this.roleService.deleteAll();
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
    public void shouldGetUserRoles() {
        // Given some new roles
        Role r1 = new Role("role1");
        Role r2 = new Role("role2");
        Role r3 = new Role("role3");
        Role r4 = new Role("role4");
        r1 = this.roleService.create(r1);
        r2 = this.roleService.create(r2);
        r3 = this.roleService.create(r3);
        r4 = this.roleService.create(r4);

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

        g1 = this.groupService.create(g1);
        g2 = this.groupService.create(g2);
        g3 = this.groupService.create(g3);
        g4 = this.groupService.create(g4);

        // Given some new users
        // u1 with direct role1 and inside group4
        User u1 = this.createTestRessource();
        u1.getRoles().add(r1); // add role1 to u1
        u1.getGroups().add(g4); // add group4 as parent of u1

        // u2 without any role and inside group3
        User u2 = this.createTestRessource();
        u2.getGroups().add(g3);

        // u3 without any role and inside group3 and group4
        User u3 = this.createTestRessource();
        u3.getGroups().add(g3);
        u3.getGroups().add(g4);

        // u4 with role4 and inside group3
        User u4 = this.createTestRessource();
        u4.getRoles().add(r4);
        u4.getGroups().add(g3);

        u1 = this.service.create(u1);
        u2 = this.service.create(u2);
        u3 = this.service.create(u3);
        u4 = this.service.create(u4);

        // When I ask for user roles
        List<Role> u1Roles = this.service.getAllUserRoles(u1.getLogin());
        List<Role> u2Roles = this.service.getAllUserRoles(u2.getLogin());
        List<Role> u3Roles = this.service.getAllUserRoles(u3.getLogin());
        List<Role> u4Roles = this.service.getAllUserRoles(u4.getLogin());

        // Then users should have the correct roles
        assertEquals("User1 should have 3 roles", 3, u1Roles.size());
        assertTrue("User1 should have role1, role2 and role4", u1Roles.contains(r1) && u1Roles.contains(r2) && u1Roles.contains(r4));
        assertEquals("User2 should have 2 roles", 2, u2Roles.size());
        assertTrue("User2 should have role1, role2 and role4", u2Roles.contains(r1) && u2Roles.contains(r3));
        assertEquals("User3 should have 4 roles", 4, u3Roles.size());
        assertTrue("User3 should have role1, role2 and role4", u3Roles.contains(r1) && u3Roles.contains(r2) && u3Roles.contains(r3) && u3Roles.contains(r4));
        assertEquals("User4 should have 3 roles", 3, u4Roles.size());
        assertTrue("User4 should have role1, role2 and role4", u4Roles.contains(r1) && u4Roles.contains(r3) && u4Roles.contains(r4));

        // TODO : remove this when we will use DBunit
        u1.getRoles().clear();
        u1.getGroups().clear();
        this.service.update(u1);
        u2.getRoles().clear();
        u2.getGroups().clear();
        this.service.update(u2);
        u3.getRoles().clear();
        u3.getGroups().clear();
        this.service.update(u3);
        u4.getRoles().clear();
        u4.getGroups().clear();
        this.service.update(u4);

        g1.getRoles().clear();
        this.groupService.update(g1);
        g2.getRoles().clear();
        this.groupService.update(g2);
        g3.getRoles().clear();
        this.groupService.update(g3);
        g4.getRoles().clear();
        this.groupService.update(g4);

        this.roleService.deleteAll();
    }

    @Test
    public void shouldAddRoleToUser() {
        // Given a new role
        Role r = new Role("Role");
        r = this.roleService.create(r);

        // Given a new user
        User u = this.createTestRessource();
        u = this.service.create(u);

        // When I associate the user and the role
        this.service.addRoleToUser(u.getLogin(), r.getName());

        // Then I get the user with this role
        User userWithRole = this.service.findById(u.getId());
        assertTrue("The user should contain the role", userWithRole.getRoles().contains(r));

        // TODO : remove this when we will use DBunit
        this.service.removeRoleFromUser(u.getLogin(), r.getName());
    }

    @Test
    public void shouldRemoveRoleFromUser() {
        // Given a new role
        Role r = new Role("Role123");
        r = this.roleService.create(r);

        // Given a new user associated to the previous role
        User u = this.createTestRessource();
        u = this.service.create(u);
        this.service.addRoleToUser(u.getLogin(), r.getName());

        // When I remove the role from the user
        this.service.removeRoleFromUser(u.getLogin(), r.getName());

        // Then I get the user without this role
        User userWithRole = this.service.findById(u.getId());
        assertFalse("The user shouldn't contain the role", userWithRole.getRoles().contains(r));

        this.roleService.deleteAll();
    }

    @Test
    public void shouldCreationBeNotified() {
        // Given a registered listener
        TestListener listener = new TestListener();
        userService.addListener(listener);

        // Given a user
        User u = new User();
        u.setLogin("user" + new Random().nextInt());

        // When saving it
        u = userService.create(u);

        // Then a creation notification has been received
        assertEquals(UserServiceChange.USER_CREATION.name(), listener.lastType);
        assertArrayEquals(new Object[]{u}, listener.lastArguments);
    } // shouldCreationBeNotified().

    @Test
    public void shouldDeletionBeNotifiedById() {
        // Given a registered listener
        TestListener listener = new TestListener();
        userService.addListener(listener);

        // Given a created user
        User u = new User();
        u.setLogin("user" + new Random().nextInt());
        u = userService.create(u);

        // When removing it by id
        userService.delete(u.getId());

        // Then a deletion notification has been received
        assertEquals(UserServiceChange.USER_DELETION.name(), listener.lastType);
        assertArrayEquals(new Object[]{u}, listener.lastArguments);
    } // shouldDeletionBeNotifiedById().

    @Test
    public void shouldDeletionBeNotifiedByUser() {
        // Given a registered listener
        TestListener listener = new TestListener();
        userService.addListener(listener);

        // Given a created user
        User u = new User();
        u.setLogin("user" + new Random().nextInt());
        u = userService.create(u);

        // When removing it
        userService.delete(u);

        // Then a deletion notification has been received
        assertEquals(UserServiceChange.USER_DELETION.name(), listener.lastType);
        assertArrayEquals(new Object[]{u}, listener.lastArguments);
    } // shouldDeletionBeNotifiedByUser().

    @Test
    public void shouldUserAdditionToGroupBeNotified() {
        // Given a registered listener
        TestListener listener = new TestListener();
        userService.addListener(listener);

        // Given a created user
        User u = new User();
        u.setLogin("user" + new Random().nextInt());
        u = userService.create(u);

        // Given a group
        Group g = new Group();
        g.setName("group" + new Random().nextInt());
        g = groupService.create(g);

        // When adding the user to the group
        userService.addGroupToUser(u.getLogin(), g.getName());

        // Then a deletion notification has been received
        assertEquals(UserServiceChange.USER_ADDED_TO_GROUP.name(), listener.lastType);
        assertArrayEquals(new Object[]{u, g}, listener.lastArguments);

        userService.removeGroupFromUser(u.getLogin(), g.getName());

    } // shouldUserAdditionToGroupBeNotified().

    @Test
    public void shouldUserRemovalFromGroupBeNotified() {
        // Given a registered listener
        TestListener listener = new TestListener();
        userService.addListener(listener);

        // Given a group
        Group g = new Group();
        g.setName("group" + new Random().nextInt());
        g = groupService.create(g);

        // Given a created user in this group
        User u = new User();
        u.setLogin("user" + new Random().nextInt());
        u = userService.create(u);
        userService.addGroupToUser(u.getLogin(), g.getName());

        // When adding the user to the group
        userService.removeGroupFromUser(u.getLogin(), g.getName());

        // Then a deletion notification has been received
        assertEquals(UserServiceChange.USER_REMOVED_FROM_GROUP.name(), listener.lastType);
        assertArrayEquals(new Object[]{u, g}, listener.lastArguments);
    } // shouldUserRemovalFromGroupBeNotified().
}
