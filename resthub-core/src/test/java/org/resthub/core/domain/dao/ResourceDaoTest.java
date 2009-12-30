package org.resthub.core.domain.dao;

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
import org.resthub.test.AbstractResthubTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = false)
public class ResourceDaoTest extends AbstractResthubTest {

    @Inject
    @Named("resourceDao")
    private ResourceDao<Resource> resourceDao;

    @Before
    public void setUp() {
        Resource resource = new Resource("Test Resource");
        resourceDao.persist(resource);
    }

    @After
    public void tearDown() {
        
    }

    @Test
    public void testPersist() throws Exception {
        Resource resource = new Resource("Test Persist Resource");
        resourceDao.persist(resource);

        Resource foundResource = resourceDao.findByName("Test Persist Resource");
        assertNotNull(foundResource);
        assertEquals("Test Persist Resource", foundResource.getName());
    }

    @Test
    public void testMerge() throws Exception {
        Resource resource = resourceDao.findByName("Test Resource");
        resource.setName("Modified Test Resource");
        resourceDao.merge(resource);

        Resource foundResource = resourceDao.findByName(resource.getName());
        assertNotNull(foundResource);
        assertEquals("Modified Test Resource", foundResource.getName());
    }

    @Test
    public void testRemove() throws Exception {
        Resource resource = resourceDao.findByName("Test Resource");
        resourceDao.remove(resource);

        Resource foundResource = resourceDao.findByName(resource.getName());
        assertNull(foundResource);
    }

    @Test
    public void testRemoveById() throws Exception {
        Resource resource = resourceDao.findByName("Test Resource");
        resourceDao.remove(resource.getId());

        Resource foundResource = resourceDao.findByName(resource.getName());
        assertNull(foundResource);
    }

    @Test
    public void testFindAll() throws Exception {
        List<Resource> resourceList = resourceDao.findAll();
        assertEquals(resourceList.size(), 1);
    }

    @Test
    public void testFindByName() throws Exception {
        Resource foundResource = resourceDao.findByName("Test Resource");
        assertNotNull(foundResource);
        assertEquals("Test Resource", foundResource.getName());
    }
}
