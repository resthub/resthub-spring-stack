package org.resthub.core.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
@ContextConfiguration(locations = {"classpath*:resthubContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional(readOnly = false)
public abstract class AbstractResourceDaoTest<T extends Resource> extends AbstractResourceClassAware<T> {

    protected ResourceDao<T> resourceDao;

    private Long resourceId;

    public void setResourceDao(ResourceDao<T> resourceDao) {
        this.resourceDao = resourceDao;
    }

    @Before
    public void setUp() throws Exception {
        T resource = resourceClass.newInstance();
        try {
            resource = resourceDao.save(resource);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.resourceId = resource.getId();
    }

    @Test
    public void testSave() throws Exception {
        T resource = resourceClass.newInstance();
        resource = resourceDao.save(resource);

        T foundResource = resourceDao.findById(resource.getId());
        assertNotNull("Resource not found!", foundResource);
    }

    @Test
    public abstract void testUpdate() throws Exception;

    @Test
    public  void testDelete() throws Exception {
        T resource = resourceDao.findById(this.resourceId);
        resourceDao.delete(resource);

        T foundResource = resourceDao.findById(this.resourceId);
        assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testDeleteById() throws Exception {
        T resource = resourceDao.findById(this.resourceId);
        resourceDao.delete(resource.getId());

        T foundResource = resourceDao.findById(this.resourceId);
        assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testFindAll() throws Exception {
        List<T> resourceList = resourceDao.findAll(0, 1);
        assertTrue("No resources found!", resourceList.size() == 1);
    }

    @Test
    public void testCount() throws Exception {
        Long nb = resourceDao.count();
        assertTrue("No resources found!", nb >= 1);
    }
}
