package org.resthub.core.service;

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

public class ResourceServiceTest extends AbstractResthubTest {
    
    @Inject
    @Named("resourceService")
    private ResourceService<Resource> resourceService;

    @Before
    public void setUp() {
        Resource resource = new Resource("Test Resource");
        resourceService.create(resource);
    }

    @After
    public void tearDown() {
        
    }

    @Test
    public void createTest() {
        Resource resource = new Resource("Test Create Resource");
        resourceService.create(resource);

        Resource foundResource = resourceService.findByName("Test Create Resource");
        assertNotNull(foundResource);
        assertEquals(resource.getName(), foundResource.getName());
    }

    @Test
    public void testMerge() throws Exception {
        Resource resource = resourceService.findByName("Test Resource");
        resource.setName("Modified Test Resource");
        resourceService.update(resource);

        Resource foundResource = resourceService.findByName(resource.getName());
        assertNotNull(foundResource);
        assertEquals("Modified Test Resource", foundResource.getName());
    }

    @Test
    public void testRemove() throws Exception {
        Resource resource = resourceService.findByName("Test Resource");
        resourceService.delete(resource);

        Resource foundResource = resourceService.findByName(resource.getName());
        assertNull(foundResource);
    }

    @Test
    public void testRemoveById() throws Exception {
        Resource resource = resourceService.findByName("Test Resource");
        resourceService.delete(resource.getId());

        Resource foundResource = resourceService.findByName(resource.getName());
        assertNull(foundResource);
    }

    @Test
    public void testFindAll() throws Exception {
        List<Resource> resourceList = resourceService.findAll();
        assertEquals(1, resourceList.size());
    }

    @Test
    public void testFindByName() throws Exception {
        Resource foundResource = resourceService.findByName("Test Resource");
        assertNotNull(foundResource);
        assertEquals("Test Resource", foundResource.getName());
    }
}
