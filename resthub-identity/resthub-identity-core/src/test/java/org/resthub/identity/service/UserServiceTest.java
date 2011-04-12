package org.resthub.identity.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.core.test.service.AbstractResourceServiceTest;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.resthub.identity.service.UserService.UserServiceChange;

public class UserServiceTest extends AbstractResourceServiceTest<User, UserService> {

    @Inject
    @Named("userService")
    @Override
    public void setResourceService(UserService resourceService) {
        super.setResourceService(resourceService);
    }

    @Inject
    @Named("groupService")
    private GroupService groupService;

    @Override
    public User createTestRessource() {
        String userLogin = "UserTestUserName" + Math.round(Math.random() * 1000);
        String userPassword = "UserTestUserPassword";
        User u = new User();
        u.setLogin(userLogin);
        u.setPassword(userPassword);
        return u;
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

        u1 = resourceService.create(u1);
        u2 = resourceService.create(u2);

        resourceService.delete(u1);
        resourceService.delete(u2);

        Assert.assertNull(resourceService.findById(u1.getId()));
        Assert.assertNull(resourceService.findById(u2.getId()));

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
        u = this.resourceService.create(u);

        // when we try to change some info (firstName) about the user and that we give the good password
        u = new User(u);
        u.setFirstName(firstNameAbr);
        u.setPassword(password);
        u = resourceService.update(u);

        // Then The modification is updated
        assertEquals(u.getFirstName(), firstNameAbr);

        this.resourceService.delete(u);
    }

    @Test
    public void shouldGetUserByAuthenticationInformationWhenOK() {
        /* Given a new user */

        String login = "alexOK";
        String password = "alex-pass";
        User u = new User();
        u.setLogin(login);
        u.setPassword(password);
        resourceService.create(u);
        // userService.create(u);

        /* When we search him with good login and password */
        // u = userService.authenticateUser(login, password);

        u = resourceService.authenticateUser(login, password);

        /* Then we retrieve the good user */
        assertNotNull(u);
        assertEquals(u.getLogin(), login);
        resourceService.delete(u);
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
        resourceService.create(u);

        /* After that the password is updates */
        u.setPassword(password2);
        u = resourceService.updatePassword(u);

        /* When we search him with good login and password */
        u = resourceService.authenticateUser(login, password2);

        /* Then we retrieve the good user */
        assertNotNull(u);
        assertEquals(u.getLogin(), login);

        resourceService.delete(u);
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
        resourceService.create(u);
        // userService.create(u);
		/* When we search him providing a bad password */
        User retrievedUser = resourceService.authenticateUser(login,
                badPassword);

        /* Then the user is not retrieved */
        assertNull(retrievedUser);

        resourceService.delete(u);
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
        resourceService.create(u);

        /* When we search him providing a bad login */
        User retrievedUser = resourceService.authenticateUser(badLogin,
                password);
        /* Then the user is not retrieved */

        assertNull(retrievedUser);

        this.resourceService.delete(u);
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
        resourceService.create(u);

        /* When we retrieved him after a search */
        u = resourceService.findByLogin(login);

        /* We can get the direct permissions */
        assertEquals("Permissions not found", 2, u.getPermissions().size());
        assertTrue("Permissions not found", u.getPermissions().contains("ADMIN"));
        assertTrue("Permissions not found", u.getPermissions().contains("USER"));

        resourceService.delete(u);

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

        User u = new User();
        u.setLogin(login);
        u.setPassword(password);
        u.getPermissions().addAll(permissions);
        u.getGroups().add(group);
        resourceService.create(u);

        /* When we retrieved him after a search */
        u = resourceService.findByLogin(login);

        /* We can get the direct permissions */
        assertEquals("Permissions not found", 2, u.getPermissions().size());
        assertTrue("Permissions not found", u.getPermissions().contains("ADMIN"));
        assertTrue("Permissions not found", u.getPermissions().contains("USER"));

        /* now with the permissions from groups */
        List<String> allPermissions = resourceService.getUserPermissions(login);
        assertEquals("Permissions not found", 4, allPermissions.size());
        assertTrue("Permissions not found", allPermissions.contains("ADMIN"));
        assertTrue("Permissions not found", allPermissions.contains("USER"));
        assertTrue("Permissions not found", allPermissions.contains("TESTGROUPPERMISSION"));
        assertTrue("Permissions not found", allPermissions.contains("TESTSUBGROUPPERMISSION"));

        resourceService.delete(u);
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

        User u = new User();
        u.setLogin(login);
        u.setPassword(password);
        u.getPermissions().addAll(permissions);
        u.getGroups().add(group);
        resourceService.create(u);

        /* When we retrieved him after a search */
        u = resourceService.findByLogin(login);

        /* We can get the direct permissions */
        assertEquals("Permissions not found", 2, u.getPermissions().size());
        assertTrue("Permissions not found", u.getPermissions().contains("ADMIN"));
        assertTrue("Permissions not found", u.getPermissions().contains("USER"));

        /* now with the permissions from groups */
        List<String> allPermissions = resourceService.getUserPermissions(login);
        assertEquals("Permissions not found", 4, allPermissions.size());
        assertTrue("Permissions not found", allPermissions.contains("ADMIN"));
        // the USER permission should exists only once in the list
        assertTrue("Permissions not found", allPermissions.contains("USER"));
        assertTrue("Permissions not found", allPermissions.contains("TESTGROUPPERMISSION"));
        assertTrue("Permissions not found", allPermissions.contains("TESTSUBGROUPPERMISSION"));

        resourceService.delete(u);
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
        u = this.resourceService.create(u);

        /* When I get the users of the group */
        List<User> usersFromGroup = this.resourceService.getUsersFromGroup(groupName);

        /* Then the list of users contains our user */
        assertTrue("The list of users should contain our just added user", usersFromGroup.contains(u));

        /* Cleanup */
        this.resourceService.delete(u);
        this.groupService.delete(g);
    }
    
    @Test
    public void shouldCreationBeNotified() {
    	// Given a registered listener
    	TestListener listener = new TestListener();
    	userService.addListener(listener);
    	
    	// Given a user
    	User u = new User();
    	u.setLogin("user"+new Random().nextInt());
    	
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
    	u.setLogin("user"+new Random().nextInt());
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
    	u.setLogin("user"+new Random().nextInt());
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
    	u.setLogin("user"+new Random().nextInt());
    	u = userService.create(u);
    	
    	// Given a group
    	Group g = new Group();
    	g.setName("group"+new Random().nextInt());
    	g = groupService.create(g);
    	
    	// When adding the user to the group
    	userService.addGroupToUser(u.getLogin(), g.getName());
    	
    	// Then a deletion notification has been received
    	assertEquals(UserServiceChange.USER_ADDED_TO_GROUP.name(), listener.lastType);
    	assertArrayEquals(new Object[]{u, g}, listener.lastArguments);    	
    } // shouldUserAdditionToGroupBeNotified().
    
    @Test
    public void shouldUserRemovalFromGroupBeNotified() {
    	// Given a registered listener
    	TestListener listener = new TestListener();
    	userService.addListener(listener);
    	
    	// Given a group
    	Group g = new Group();
    	g.setName("group"+new Random().nextInt());
    	g = groupService.create(g);

    	// Given a created user in this group
    	User u = new User();
    	u.setLogin("user"+new Random().nextInt());
    	u = userService.create(u);   	
    	userService.addGroupToUser(u.getLogin(), g.getName());
   	
    	// When adding the user to the group
    	userService.removeGroupFromUser(u.getLogin(), g.getName());
    	
    	// Then a deletion notification has been received
    	assertEquals(UserServiceChange.USER_REMOVED_FROM_GROUP.name(), listener.lastType);
    	assertArrayEquals(new Object[]{u, g}, listener.lastArguments);    	
    } // shouldUserRemovalFromGroupBeNotified().
}
