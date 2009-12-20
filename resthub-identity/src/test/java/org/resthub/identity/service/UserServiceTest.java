package org.resthub.identity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.domain.model.User;
import org.resthub.core.service.ResourceService;
import org.resthub.test.AbstractResthubTest;

public class UserServiceTest extends AbstractResthubTest {
    
    @Inject
    @Named("userService")
    private ResourceService<User> userService;

    @Before
    public void setUp() {
        User user = new User("TestName");
        userService.create(user);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testPersist() throws Exception {
        User user = new User("TestName");
        userService.create(user);

        Resource foundResource = userService.findByName("TestName");
        assertNotNull(foundResource);
        assertEquals("TestName", foundResource.getName());
    }

    @Test
    public void testMerge() throws Exception {
        User user = userService.findByName("TestName");
        user.setName("Modified Test Resource");
        userService.update(user);

        Resource foundResource = userService.findByName(user.getName());
        assertNotNull(foundResource);
        assertEquals("Modified Test Resource", foundResource.getName());
    }

    @Test
    public void testRemove() throws Exception {
        User user = userService.findByName("TestName");
        userService.delete(user);

        Resource foundResource = userService.findByName(user.getName());
        assertNull(foundResource);
    }

    @Test
    public void testRemoveById() throws Exception {
        User user = userService.findByName("TestName");
        userService.delete(user.getId());

        Resource foundResource = userService.findByName(user.getName());
        assertNull(foundResource);
    }

    @Test
    public void testFindAll() throws Exception {
        List<User> userList = userService.findAll();
        assertEquals(userList.size(), 1);
    }

    @Test
    public void testFindByName() throws Exception {
        User founduser = userService.findByName("TestName");
        assertNotNull(founduser);
        assertEquals("TestName", founduser.getName());
    }
}
