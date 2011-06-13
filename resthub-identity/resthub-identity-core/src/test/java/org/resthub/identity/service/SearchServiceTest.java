package org.resthub.identity.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.AbstractTransactionAwareTest;
import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;

/**
 * SearchService test
 */
public class SearchServiceTest extends AbstractTransactionAwareTest {

    /**
     * Injection of the tested service.
     */
    @Inject
    protected SearchService tested;

    /**
     * Injection of the User service.
     */
    @Inject
    @Named("userService")
    protected UserService userService;

    /**
     * Injection of the Group service.
     */
    @Inject
    @Named("groupService")
    protected GroupService groupService;

    @Test
    public void shouldExistingResourcesBeReIndexed() {
        // Given an existing group
        Group g1 = new Group();
        g1.setName("group 1" + new Random().nextInt());
        g1 = groupService.create(g1);

        // Given an existing user
        User u1 = new User();
        u1.setLogin("user 1" + new Random().nextInt());
        u1.setPassword(u1.getLogin());
        u1 = userService.create(u1);

        // TODO Given an existing role

        // When reindexing
        tested.resetIndexes();

        // Then everything's fine
    } // shouldExistingResourcesBeReIndexed().

    @Test
    public void shouldUsersBeRetrieved() {
        String pwd = "password";
        String login = "login";

        // Given a user with login 'jdujardin'
        User u1 = new User();
        u1.setLogin("jdujardin" + new Random().nextInt());
        u1.setPassword(pwd);
        u1 = userService.create(u1);

        // Given a user with email 'jdujardin@test.com'
        User u2 = new User();
        u2.setLogin(login + new Random().nextInt());
        u2.setEmail("jdujardin@test.com");
        u2.setPassword(pwd);
        u2 = userService.create(u2);

        // Given a user with first name 'jean'
        User u3 = new User();
        u3.setLogin(login + new Random().nextInt());
        u3.setFirstName("jean");
        u3.setPassword(pwd);
        u3 = userService.create(u3);

        // Given a user with last name 'dujardin'
        User u4 = new User();
        u4.setLogin(login + new Random().nextInt());
        u4.setLastName("dujardin");
        u4.setPassword(pwd);
        u4 = userService.create(u4);

        // Given a user with login 'adurand'
        User u5 = new User();
        u5.setLogin(login + new Random().nextInt());
        u5.setLogin("adurand");
        u5.setPassword(pwd);
        u5 = userService.create(u5);

        // When requesting j on users
        List<AbstractPermissionsOwner> results = tested.search("j", true, false);

        // Then the first user is retrieved
        assertTrue("login 'jdujardin' did not match", results.contains(u1));

        // Then the second user is retrieved
        assertTrue("email 'jdujardin@test.com' did not match", results.contains(u2));

        // Then the third user is retrieved
        assertTrue("first name 'jean' did not match", results.contains(u3));

        // Then the fourth user is not retrived
        assertFalse("last name 'dujardin' did match", results.contains(u4));

        // Then the fifth user is not retrived
        assertFalse("login 'adurand' did match", results.contains(u5));
    } // shouldUsersBeRetrieved().

    @Test
    public void shouldGroupsBeRetrieved() {
        // Given a group with name 'jeans'
        Group g1 = new Group();
        g1.setName("jdujardin" + new Random().nextInt());
        g1 = groupService.create(g1);

        // Given a group with name 'dujeu'
        Group g2 = new Group();
        g2.setName("dujeu" + new Random().nextInt());
        g2 = groupService.create(g2);

        // Given a group with login 'other'
        Group g3 = new Group();
        g3.setName("other" + new Random().nextInt());
        g3 = groupService.create(g3);

        // When requesting j on users
        List<AbstractPermissionsOwner> results = tested.search("j", false, true);

        // Then the first group is retrieved
        assertTrue("login 'jeans' did not match", results.contains(g1));

        // Then the second group is not retrived
        assertFalse("name 'dujeu' did match", results.contains(g2));

        // Then the third group is not retrived
        assertFalse("name 'other' did match", results.contains(g3));
    } // shouldGroupsBeRetrieved().

    @Test
    public void shouldUserAndGroupsBeRetrieved() {
        String pwd = "password";

        // Given a user with login 'jdujardin'
        User u1 = new User();
        u1.setLogin("jdujardin" + new Random().nextInt());
        u1.setPassword(pwd);
        u1 = userService.create(u1);

        // Given a user with first name 'other'
        User u2 = new User();
        u2.setLogin("other" + new Random().nextInt());
        u2.setPassword(pwd);
        u2 = userService.create(u2);

        // Given a group with name 'jeans'
        Group g1 = new Group();
        g1.setName("jeans" + new Random().nextInt());
        g1 = groupService.create(g1);

        // Given a group with login 'other-group'
        Group g2 = new Group();
        g2.setName("other-group" + new Random().nextInt());
        g2 = groupService.create(g2);

        // When requesting j on users and groups
        List<AbstractPermissionsOwner> results = tested.search("j", true, true);

        // Then the first user is retrieved
        assertTrue("login 'jdujardin' did not match", results.contains(u1));

        // Then the second user is not retrieved
        assertFalse("login 'other' did match", results.contains(u2));

        // Then the first group is retrieved
        assertTrue("login 'jeans' did not match", results.contains(g1));

        // Then the second group is not retrived
        assertFalse("name 'other-group' did match", results.contains(g2));
    } // shouldUserAndGroupsBeRetrieved()

    @Test
    public void shouldComplexQueriesBeExecuted() {
        String pwd = "password";

        // Given a user with login 'dujardin' and first name 'jean'
        User u1 = new User();
        u1.setLogin("dujardin" + new Random().nextInt());
        u1.setFirstName("jean");
        u1.setPassword(pwd);
        u1 = userService.create(u1);

        // Given a user with login 'other' abd last name 'george'
        User u2 = new User();
        u2.setLogin("other" + new Random().nextInt());
        u2.setLastName("george");
        u2.setPassword(pwd);
        u2 = userService.create(u2);

        // Given a group with name 'admin'
        Group g1 = new Group();
        g1.setName("admin" + new Random().nextInt());
        g1 = groupService.create(g1);

        // Given a group with login 'users'
        Group g2 = new Group();
        g2.setName("users" + new Random().nextInt());
        g2 = groupService.create(g2);

        // When requesting j on users and groups
        List<AbstractPermissionsOwner> results = tested
                .search("lastName:g* OR firstName:j* OR name:admin*", true, true);

        // Then the first user is retrieved
        assertTrue("last name 'jean' did not match", results.contains(u1));

        // Then the second user is retrieved
        assertTrue("first name 'george' did not match", results.contains(u2));

        // Then the first group is retrieved
        assertTrue("name 'admin' did not match", results.contains(g1));

        // Then the second group is not retrived
        assertFalse("name 'users' did match", results.contains(g2));
    } // shouldComplexQueriesBeExecuted()

    @Test
    public void shouldFailedOnNullQuery() {
        try {
            // When performing a search with a null query
            tested.search(null, false, false);
            fail("An IllegalArgumentException may have been raised");
        } catch (IllegalArgumentException exc) {
            // Then an exception is raised
            assertTrue(exc.getMessage().contains("must not be null"));
        }
    } // shouldFailedOnNullQuery()

    @Test
    public void shouldFailedOnInvalidQuery() {
        try {
            // When performing a search with a invalide query
            tested.search("name:*", false, false);
            fail("An IllegalArgumentException may have been raised");
        } catch (IllegalArgumentException exc) {
            // Then an exception is raised
            assertTrue(exc.getMessage().toLowerCase().contains("misformatted"));
        }
    } // shouldFailedOnInvalidQuery()

} // Class SearchDaoTest.
