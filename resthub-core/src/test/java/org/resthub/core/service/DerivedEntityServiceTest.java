package org.resthub.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.domain.model.DerivedEntity;
import org.resthub.core.test.AbstractResourceServiceTest;

public class DerivedEntityServiceTest extends AbstractResourceServiceTest<DerivedEntity, DerivedEntityService> {

	@Inject
    @Named("derivedEntityService")
    @Override
	public void setResourceService(
			DerivedEntityService resourceService) {
		// TODO Auto-generated method stub
		super.setResourceService(resourceService);
	}

	@Test
	public void testFindByName() throws Exception {
		
		DerivedEntity derivedEntity = new DerivedEntity();
		derivedEntity.setName("test");
		derivedEntity = this.resourceService.create(derivedEntity);
		
		List<DerivedEntity> entities = this.resourceService.findByName("test");
		assertNotNull("entities should not be null", entities);
		assertFalse("entities should not be empty", entities.isEmpty());
		assertTrue("entities size should be exactly 1", entities.size() == 1);
		assertEquals("entitie name should be 'test'","test", entities.get(0).getName());
	}
	
	@Override
    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
