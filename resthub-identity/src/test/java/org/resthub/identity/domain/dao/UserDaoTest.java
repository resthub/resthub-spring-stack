package org.resthub.identity.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.User;
import org.resthub.test.AbstractResthubTest;

/**
 *
 * @author Loïc Frering <loic.frering@atosorigin.com>
 */
public class UserDaoTest extends AbstractResthubTest {

    @Inject
    @Named("userDao")
    private ResourceDao<User> userDao;

    @Before
    public void setUp() {
        
    }

    @After
    public void tearDown() {
        
    }

    @Test
    public void testPersist() throws Exception {
        User user = new User("TestPersistName");

        userDao.persist(user);

        User foundUser = userDao.findByName("TestPersistName");
        assertNotNull(foundUser);
        assertEquals("TestPersistName", foundUser.getName());
       
    }
}
