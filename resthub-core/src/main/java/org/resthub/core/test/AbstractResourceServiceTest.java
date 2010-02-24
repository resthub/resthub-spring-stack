package org.resthub.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
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
@ContextConfiguration(locations = { "classpath*:resthubContext.xml","classpath:resthubContext.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class AbstractResourceServiceTest<T extends Resource> extends AbstractResourceClassAware<T> {
    
    protected ResourceService<T> resourceService;
    
    public void setResourceService(ResourceService<T> resourceService) {
        this.resourceService = resourceService;
    }
    
    @Before
    public void setUp() throws Exception {
        T resource = resourceClass.newInstance();
        resource.setName("Test T");
        resourceService.create(resource);
    }

    @After
    public void tearDown() {
        
    }

    @Test
    public void createTest() throws Exception {
    	T resource = resourceClass.newInstance();
        resource.setName("Test Create T");
        resourceService.create(resource);

        T foundResource = resourceService.findByName("Test Create T");
        assertNotNull(foundResource);
        assertEquals(resource.getName(), foundResource.getName());
    }

    @Test
    public void testMerge() throws Exception {
        T resource = resourceService.findByName("Test T");
        resource.setName("Modified Test T");
        resourceService.update(resource);

        T foundResource = resourceService.findByName(resource.getName());
        assertNotNull(foundResource);
        assertEquals("Modified Test T", foundResource.getName());
    }

    @Test
    public void testRemove() throws Exception {
        T resource = resourceService.findByName("Test T");
        resourceService.delete(resource);

        T foundResource = resourceService.findByName(resource.getName());
        assertNull(foundResource);
    }

    @Test
    public void testRemoveById() throws Exception {
        T resource = resourceService.findByName("Test T");
        resourceService.delete(resource.getId());

        T foundResource = resourceService.findByName(resource.getName());
        assertNull(foundResource);
    }

    @Test
    public void testFindAll() throws Exception {
        List<T> resourceList = resourceService.findAll();
        assertEquals(1, resourceList.size());
    }

    @Test
    public void testFindByName() throws Exception {
        T foundResource = resourceService.findByName("Test T");
        assertNotNull(foundResource);
        assertEquals("Test T", foundResource.getName());
    }
}
