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

/**
 * This Test Suite performs tests on {@link StandaloneEntity} class in order to
 * validate services behaviours with an entity
 */
public class StandaloneEntityServiceTest extends AbstractServiceTest<StandaloneEntity, Long, StandaloneEntityService> {

	private static final String ENTITY_NAME = "Name";
	private static final String ENTITY_NEW_NAME = "New name";

	/**
	 * Test entity
	 */
	private StandaloneEntity standaloneEntity;

	@Inject
	@Named("standaloneEntityService")
	public void setResourceService(StandaloneEntityService service) {
		super.setService(service);
	}

	@Override
	protected StandaloneEntity createTestEntity() {
		standaloneEntity = new StandaloneEntity();
		standaloneEntity.setName(ENTITY_NAME);
		return standaloneEntity;
	}

	@Test
	public void testFindByName() throws Exception {
		List<StandaloneEntity> entities = this.service.findByName(ENTITY_NAME);
		assertNotNull("entities should not be null", entities);
		assertFalse("entities should not empty", entities.isEmpty());
		assertEquals("entities size should be exactly 1", 1, entities.size());
		assertEquals("entitie name should be " + ENTITY_NAME, ENTITY_NAME, entities.get(0).getName());
	}

	@Override
	@Test
	public void testUpdate() {
		StandaloneEntity testStandaloneEntity = service.findById(standaloneEntity.getId());
		testStandaloneEntity.setName(ENTITY_NEW_NAME);
		service.update(testStandaloneEntity);

		StandaloneEntity updatedEntity = service.findById(standaloneEntity.getId());
		assertEquals("Entity name should have been modified", ENTITY_NEW_NAME, updatedEntity.getName());
	}
}
