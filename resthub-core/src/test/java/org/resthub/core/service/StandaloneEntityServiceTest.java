package org.resthub.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.model.StandaloneEntity;
import org.resthub.core.test.service.AbstractServiceTest;

public class StandaloneEntityServiceTest extends
		AbstractServiceTest<StandaloneEntity, Long, StandaloneEntityService> {

	@Inject
	@Named("standaloneEntityService")
	public void setResourceService(StandaloneEntityService service) {
		super.setService(service);
	}

	@Override
	protected StandaloneEntity createTestRessource() {
		StandaloneEntity standaloneEntity = new StandaloneEntity();
		standaloneEntity.setName("test");
		return standaloneEntity;
	}

	@Test
	public void testFindByName() throws Exception {
		List<StandaloneEntity> entities = this.service
				.findByName("test");
		assertNotNull("entities should not be null", entities);
		assertFalse("entities should not empty", entities.isEmpty());
		assertEquals("entities size should be exactly 1", 1, entities.size());
		assertEquals("entitie name should be 'test'", "test", entities.get(0)
				.getName());
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testUpdate() throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
