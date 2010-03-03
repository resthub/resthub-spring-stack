package org.resthub.core.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resthub.core.AbstractResourceClassAware;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.ResourceService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:resthubContext.xml", "classpath:resthubContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional(readOnly = false)
public abstract class AbstractResourceServiceTest<T extends Resource> extends AbstractResourceClassAware<T> {

    protected ResourceService<T> resourceService;

    private Long resourceId;

    public void setResourceService(ResourceService<T> resourceService) {
        this.resourceService = resourceService;
    }

    @Before
    public void setUp() throws Exception {
        T resource = resourceClass.newInstance();
        resource = resourceService.create(resource);
        this.resourceId = resource.getId();
    }

    @Test
    public void testCreate() throws Exception {
        T resource = resourceClass.newInstance();
        resource = resourceService.create(resource);

        T foundResource = resourceService.findById(resource.getId());
        assertNotNull("Resource not created!", foundResource);
    }

    @Test
    public abstract void testUpdate() throws Exception;

    @Test
    public void testDelete() throws Exception {
        T resource = resourceService.findById(this.resourceId);
        resourceService.delete(resource);

        T foundResource = resourceService.findById(this.resourceId);
        assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testDeleteById() throws Exception {
        T resource = resourceService.findById(this.resourceId);
        resourceService.delete(resource.getId());

        T foundResource = resourceService.findById(this.resourceId);
        assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testFindAll() throws Exception {
        List<T> resourceList = resourceService.findAll(null, null);
        assertTrue("No resources found!", resourceList.size() >= 1);
    }

    @Test
    public void testCount() throws Exception {
        Long nb = resourceService.count();
        assertTrue("No resources found!", nb >= 1);
    }
}
