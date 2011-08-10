package org.resthub.core.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.model.StandaloneEntity;
import org.resthub.core.test.repository.AbstractRepositoryTest;

/**
 * This Test Suite performs tests on {@link StandaloneEntity} class in order to validate repositories behaviours with an
 * entity
 */
public class StandaloneEntityRepositoryTest extends
		AbstractRepositoryTest<StandaloneEntity, Long, StandaloneEntityRepository> {

	private static final String ENTITY_NAME = "Name";
	private static final String ENTITY_NEW_NAME = "New name";

	/**
	 * Test entity
	 */
	private StandaloneEntity standaloneEntity;

	/**
	 * {@inheritDoc}
	 */
	@Inject
	@Named("standaloneEntityRepository")
	public void setRepository(StandaloneEntityRepository standaloneEntityRepository) {
		super.repository = standaloneEntityRepository;
	}

	@Override
	protected StandaloneEntity createTestEntity() {
		standaloneEntity = new StandaloneEntity();
		standaloneEntity.setName(ENTITY_NAME);

		return standaloneEntity;
	}

	@Override
	@Test
	public void testUpdate() {
		StandaloneEntity testStandaloneEntity = repository.findOne(standaloneEntity.getId());
		testStandaloneEntity.setName(ENTITY_NEW_NAME);
		repository.saveAndFlush(testStandaloneEntity);

		StandaloneEntity updatedEntity = repository.findOne(standaloneEntity.getId());
		assertEquals("Entity name should have been modified", ENTITY_NEW_NAME, updatedEntity.getName());
	}

	@Test
	public void testFindByName() {
		List<StandaloneEntity> entities = repository.findByName(ENTITY_NAME);

		assertNotNull("Entities should not be null", entities);
		assertFalse("Entities should not empty", entities.isEmpty());
	}
}
