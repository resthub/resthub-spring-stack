package org.resthub.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.test.AbstractResourceServiceTest;

public class ResourceServiceTest extends AbstractResourceServiceTest<Resource, ResourceService> {

	@Inject
    @Named("resourceService")
    @Override
	public void setResourceService(
			ResourceService resourceService) {
		// TODO Auto-generated method stub
		super.setResourceService(resourceService);
	}
	
	@Test
	public void testFindByRef() throws Exception {
		
		Resource r = new Resource();
		r.setRef("test");
		r = this.resourceService.create(r);
		
		List<Resource> entities = this.resourceService.findByRef("test");
		assertNotNull("entities should not be null", entities);
		assertFalse("entities should not be empty", entities.isEmpty());
		assertTrue("entities size should be exactly 1", entities.size() == 1);
		assertEquals("entitie ref should be 'test'","test", entities.get(0).getRef());
	}

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
