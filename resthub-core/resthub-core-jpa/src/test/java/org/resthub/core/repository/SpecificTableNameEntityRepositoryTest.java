package org.resthub.core.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.model.SpecificTableNameEntity;
import org.resthub.core.test.repository.AbstractRepositoryTest;

/**
 * This Test Suite performs tests on {@link SpecificTableNameEntity} class in order to validate repositories behaviours
 * with entities named differently than their classes
 */
public class SpecificTableNameEntityRepositoryTest extends
        AbstractRepositoryTest<SpecificTableNameEntity, Long, SpecificTableNameEntityRepository> {

    private static final String ENTITY_NAME = "Name";
    private static final String ENTITY_NEW_NAME = "New name";

    private SpecificTableNameEntity specificTableNameEntity;

    @Inject
    @Named("specificTableNameEntityRepository")
    public void setRepository(SpecificTableNameEntityRepository specificTableNameEntityRepository) {
        super.repository = specificTableNameEntityRepository;
    }

    @Override
    public Long getIdFromEntity(SpecificTableNameEntity specificTableNameEntity) {
        return specificTableNameEntity.getId();
    }

    @Override
    protected SpecificTableNameEntity createTestEntity() {
        specificTableNameEntity = new SpecificTableNameEntity();
        specificTableNameEntity.setName(ENTITY_NAME);

        return specificTableNameEntity;
    }

    @Override
    @Test
    public void testUpdate() {
        SpecificTableNameEntity testSpecificTableNameEntity = repository.findOne(specificTableNameEntity.getId());
        testSpecificTableNameEntity.setName(ENTITY_NEW_NAME);
        repository.saveAndFlush(testSpecificTableNameEntity);

        SpecificTableNameEntity updatedEntity = repository.findOne(specificTableNameEntity.getId());
        assertEquals("Entity name should have been modified", ENTITY_NEW_NAME, updatedEntity.getName());
    }

    @Test
    public void testFindByName() {
        List<SpecificTableNameEntity> entities = repository.findByName(ENTITY_NAME);

        assertNotNull("Entities should not be null", entities);
        assertFalse("Entities should not empty", entities.isEmpty());
    }
}
