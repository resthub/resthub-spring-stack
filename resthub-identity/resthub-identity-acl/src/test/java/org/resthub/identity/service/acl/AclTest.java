package org.resthub.identity.service.acl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.test.AbstractTest;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;
import org.resthub.identity.service.GroupService;
import org.resthub.identity.service.UserService;
import org.resthub.identity.service.acl.AclService.AclServiceChange;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * First unit test in order to test Spring ACL support
 * 
 * Interesting Pointers : - Spring Security ACL reference documentation :
 * http://
 * static.springsource.org/spring-security/site/docs/3.0.x/reference/domain
 * -acls.html - Spring Security ACL example - Interface :
 * https://fisheye.springsource
 * .org/browse/~tag=3.0.5.RELEASE/spring-security/samples
 * /contacts/src/main/java/sample/contact/ContactManager.java?r=052537
 * c8b04182595e92abd1e1949b0ff7e731b4
 * &r=90304f64c63562ab20c24c5f014a7f55d7d883d2&
 * r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4 - Implementation :
 * https://fisheye
 * .springsource.org/browse/~tag=3.0.5.RELEASE/spring-security/samples
 * /contacts/src/main/java/sample/contact/ContactManagerBackend.java?r=052537
 * c8b04182595e92abd1e1949b0ff7e731b4
 * &r=90304f64c63562ab20c24c5f014a7f55d7d883d2&
 * r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4
 * &r=052537c8b04182595e92abd1e1949b0ff7e731b4 - Igenko user and permission
 * adapters for Spring Security : -
 * http://code.google.com/p/igenko/source/browse
 * /trunk/igenko-core/src/main/resources/appCtxSecurity.xml -
 * http://code.google.
 * com/p/igenko/source/browse/trunk/igenko-core/src/main/java/
 * org/igenko/core/security/IgenkoUserDetailsService.java -
 * http://code.google.com
 * /p/igenko/source/browse/trunk/igenko-core/src/main/java/
 * org/igenko/core/security/UserDetailsAdapter.java
 * 
 * TODO : - Fix testAuthorizedDelete : it throw an AccessDeniedException, but it
 * should'nt - Choose between the following 2 strategies : - Use Spring ACL to
 * store all permissions, related to a model instance or not (I prefer this one)
 * - Keep using permission not related to a model instance in the current model,
 * and use Sprign ACL only for permissions related to a model instance - Update
 * RESThub OAuth2 to support Spring Security
 * 
 */
public class AclTest extends AbstractTest {

    @Inject
    @Named("idmAclService")
    private AclService aclService;

    @Inject
    @Named("securedGroupService")
    private SecuredGroupService securedGroupService;

    @Inject
    @Named("groupService")
    private GroupService groupService;

    @Inject
    @Named("userService")
    private UserService userService;

    private Long group1Id;

    protected Authentication auth = new TestingAuthenticationToken("joe", "ignored", "ROLE_ADMINISTRATOR");

    @Before
    // Create database tables if needed
    public void init() throws IOException {
        // Inspired from
        // https://fisheye.springsource.org/browse/~raw,r=052537c8b04182595e92abd1e1949b0ff7e731b4/spring-security/acl/src/test/java/org/springframework/security/acls/domain/AclImplTests.java
        SecurityContextHolder.getContext().setAuthentication(auth);
        auth.setAuthenticated(true);

        // Create test users
        if (userService.findByLogin("test") == null) {
            User test = new User();
            test.setLogin("test");
            test.setPassword("t3st");
            test.setFirstName("test");
            test.setLastName("ing");
            test.setEmail("test@resthub.org");
            test.getPermissions().add("IM-USER");
            test.getPermissions().add("CREATE");
            userService.create(test);
        }

        // Create test users
        if (userService.findByLogin("admin") == null) {
            User admin = new User();
            admin.setLogin("admin");
            admin.setPassword("4dm|n");
            admin.setFirstName("alex");
            admin.setLastName("synclar");
            admin.setEmail("user1@resthub.org");
            admin.getPermissions().add("IM-ADMIN");
            userService.create(admin);
        }

        // Create Wax Taylor with no permissions on it
        Group wt = new Group();
        wt.setName("Wax Taylor 2 le retour");
        groupService.create(wt);
        group1Id = wt.getId();

        // Create Hocus Pocus group, and give Joe access to it
        Group hp = new Group();
        hp.setName("Hocus Pocus 2 le retour");
        hp = groupService.create(hp);

        aclService.saveAcl(hp, hp.getId(), "joe", "CUSTOM");
    }

    @After
    public void clean() {
        groupService.deleteAll();
    }

    @Test
    public void testAuthorizedDelete() {

        Group g = groupService.findByName("Hocus Pocus 2 le retour");
        securedGroupService.delete(g);
    }

    @Test(expected = AccessDeniedException.class)
    public void testUnauthorizedDelete() {
        Group g = groupService.findByName("Wax Taylor 2 le retour");
        securedGroupService.delete(g);
    }

    @Test
    public void shouldAclCreationBeNotified() {
        // Given a registered listener
        TestListener listener = new TestListener();
        aclService.addListener(listener);

        // Given a created group and domain object
        Group g1 = groupService.findById(group1Id);
        String userId = "kac" + new Random().nextInt();
        String permission = "DELETE";

        // When creating an acl
        aclService.saveAcl(g1, group1Id, userId, permission);

        // Then a creation notification has been received
        assertEquals(AclServiceChange.ACL_CREATION.name(), listener.lastType);
        assertArrayEquals(new Object[] { group1Id, userId, permission }, listener.lastArguments);
    } // shouldAclCreationBeNotified().

    @Test
    public void shouldAclDeletionBeNotified() {
        // Given a registered listener
        TestListener listener = new TestListener();
        aclService.addListener(listener);

        // Given a created group and domain object
        Group g1 = groupService.findById(group1Id);
        String userId = "kac" + new Random().nextInt();
        String permission = "DELETE";

        // Given an acl
        aclService.saveAcl(g1, group1Id, userId, permission);

        // When deleting the acl
        aclService.removeAcl(g1, group1Id, userId, permission);

        // Then a deletion notification has been received
        assertEquals(AclServiceChange.ACL_DELETION.name(), listener.lastType);
        assertArrayEquals(new Object[] { group1Id, userId, permission }, listener.lastArguments);
    } // shouldAclDeletionBeNotified().

    @Test
    public void addViewerPermission() {

        List<String> permissions = new ArrayList<String>();
        permissions.add("CREATE");
        permissions.add("WRITE");
        permissions.add("READ");

        // Given a created group and domain object
        Group g1 = groupService.findById(group1Id);
        String userId = "userId" + new Random().nextInt();

        // When creating an acl
        aclService.saveAcls(g1, group1Id, userId, permissions);
        Acl foudedAcl = aclService.getAcls(g1, group1Id);
        Assert.assertNotNull(foudedAcl);
        Assert.assertEquals(permissions.size(), foudedAcl.getEntries().size());
    }

    @Test
    public void testAddPermission() {

        Permission permission = BasePermission.READ;
        // Given a created group and domain object
        Group g1 = groupService.findById(group1Id);
        String userId = "userId" + new Random().nextInt();

        // When creating an acl
        aclService.addPermission(g1, new PrincipalSid(userId), permission);
        Acl foundAcl = aclService.getAcls(g1, group1Id);
        Assert.assertNotNull(foundAcl);
        Assert.assertEquals(1, foundAcl.getEntries().size());
    }

    @Test
    public void testDeletePermission() {

        List<String> permissions = new ArrayList<String>();
        permissions.add("CREATE");
        permissions.add("WRITE");
        permissions.add("READ");

        // Given a created group and domain object
        Group g1 = groupService.findById(group1Id);
        String userId = "userId" + new Random().nextInt();

        // When creating an acl
        aclService.saveAcls(g1, group1Id, userId, permissions);
        Acl acl = aclService.getAcls(g1, group1Id);
        Assert.assertNotNull(acl);
        Assert.assertEquals(permissions.size(), acl.getEntries().size());
        // Delete permission entry
        aclService.deletePermission(g1, new PrincipalSid(userId), BasePermission.CREATE);
        Assert.assertNotNull(acl);
        Assert.assertEquals(permissions.size() - 1, acl.getEntries().size());

    }

}
