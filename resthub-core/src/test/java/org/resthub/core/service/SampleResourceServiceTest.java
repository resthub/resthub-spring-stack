package org.resthub.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.model.SampleResource;
import org.resthub.core.test.AbstractResourceServiceTest;

public class SampleResourceServiceTest extends AbstractResourceServiceTest<SampleResource, SampleResourceService> {

	@Inject
    @Named("sampleResourceService")
    @Override
	public void setResourceService(
			SampleResourceService resourceService) {
		// TODO Auto-generated method stub
		super.setResourceService(resourceService);
	}
	
	@Test
	public void testFindByRef() throws Exception {
		
		SampleResource r = new SampleResource();
		r.setRef("test");
		r = this.resourceService.create(r);
		
		List<SampleResource> entities = this.resourceService.findByRef("test");
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
