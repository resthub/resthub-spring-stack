package org.resthub.identity.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.dao.AbstractDaoTest;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;

/**
 * 
 * @author Guillaume Zurbach
 */
public class UserDaoTest extends AbstractDaoTest<User, Long, UserDao> {

    @Inject
    @Named("roleDao")
    protected RoleDao roleDao;
    @Inject
    @Named("groupDao")
    protected PermissionsOwnerDao<Group> groupDao;

    @Inject
    @Named("userDao")
    @Override
    public void setDao(UserDao resourceDao) {
        super.setDao(resourceDao);
    }

    @Override
    protected User createTestEntity() {
        String userLogin = "UserTestUserLogin" + Math.round(Math.random() * 100000);
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

    @Override
    public void testUpdate() {
        User u1 = dao.readByPrimaryKey(this.id);
        u1.setEmail("test@plop.fr");
        dao.save(u1);
        User u2 = dao.readByPrimaryKey(this.id);
        assertEquals("User not updated!", u2.getEmail(), "test@plop.fr");
    }

    @Test
    public void testGetANDPermissions() {
        // given a new user
        User u1 = new User();
        String login = "alexDao";
        String password = "alexDao-pass";
        u1.setLogin(login);
        u1.setPassword(password);
        ArrayList<String> permissions = new ArrayList<String>();
        permissions.add("ADMIN");
        permissions.add("USER");
        u1.getPermissions().addAll(permissions);

        dao.save(u1);

        // when we search him by his login and password
        List<User> l = dao.findEquals("Login", login);
        assertNotNull(l);

        u1 = l.get(0);
        // we get the user as response
        assertNotNull(u1);
        assertEquals(login, u1.getLogin());
        assertEquals(u1.getPermissions().get(0), "ADMIN");

        // TODO : remove this when we will use DBunit
        u1.getPermissions().clear();
    }
}
