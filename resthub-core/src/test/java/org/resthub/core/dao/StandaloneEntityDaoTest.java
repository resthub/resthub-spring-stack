package org.resthub.core.dao;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.model.StandaloneEntity;
import org.resthub.core.test.dao.AbstractDaoTest;

/**
 * This Test Suite performs tests on {@link StandaloneEntity} class in order to
 * validate generic daos and services behaviours with a non resource dervied
 * entity
 */
public class StandaloneEntityDaoTest extends AbstractDaoTest<StandaloneEntity, Long, StandaloneEntityDao> {

    @Inject
    @Named("standaloneEntityDao")
    @Override
    public void setDao(StandaloneEntityDao dao) {
        this.dao = dao;
    }

    @Override
    @Test
    public void testUpdate() {
        StandaloneEntity entity = new StandaloneEntity();
        entity.setName("Name");
        dao.saveAndFlush(entity);

        entity.setName("New name");
        dao.saveAndFlush(entity);

        StandaloneEntity updatedEntity = dao.readByPrimaryKey(entity.getId());
        assertEquals("Entity name should have been modified", "New name", updatedEntity.getName());
    }
}
