package org.resthub.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resthub.core.AbstractResourceClassAware;
import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:resthubContext.xml","classpath:resthubContext.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional(readOnly = false)
public abstract class AbstractResourceDaoTest<T extends Resource> extends AbstractResourceClassAware<T> {

	protected ResourceDao<T> resourceDao;
    
    public void setResourceDao(ResourceDao<T> resourceDao) {
        this.resourceDao = resourceDao;
    }
    
    @Before
    public void setUp() throws Exception {
        T resource = resourceClass.newInstance();
        resource.setName("Test T");
        resourceDao.persist(resource);
    }
    
    @Test
    public void testPersist() throws Exception {
        T resource = resourceClass.newInstance();
        resource.setName("Test Persist T");
        resourceDao.persist(resource);

        T foundResource = resourceDao.findByName("Test Persist T");
        assertNotNull(foundResource);
        assertEquals("Test Persist T", foundResource.getName());
    }

    @Test
    public void testMerge() throws Exception {
        T resource = resourceDao.findByName("Test T");
        resource.setName("Modified Test T");
        resourceDao.merge(resource);

        T foundResource = resourceDao.findByName(resource.getName());
        assertNotNull(foundResource);
        assertEquals("Modified Test T", foundResource.getName());
    }

    @Test
    public void testRemove() throws Exception {
        T resource = resourceDao.findByName("Test T");
        resourceDao.remove(resource);

        T foundResource = resourceDao.findByName(resource.getName());
        assertNull(foundResource);
    }

    @Test
    public void testRemoveById() throws Exception {
        T resource = resourceDao.findByName("Test T");
        resourceDao.remove(resource.getId());

        T foundResource = resourceDao.findByName(resource.getName());
        assertNull(foundResource);
    }

    @Test
    public void testFindAll() throws Exception {
        List<T> resourceList = resourceDao.findAll();
        assertEquals(resourceList.size(), 1);
    }

    @Test
    public void testFindByName() throws Exception {
        T foundResource = resourceDao.findByName("Test T");
        assertNotNull(foundResource);
        assertEquals("Test T", foundResource.getName());
    }
}
