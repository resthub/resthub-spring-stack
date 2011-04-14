package org.resthub.core.dao;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.model.StandaloneEntity;

/**
 * @author bmeurant <baptiste.meurant@gmail.com>
 * @author Loïc Frering <loic.frering@gmail.com>
 * 
 *         This Test Suite performs tests on {@link StandaloneEntity} class in
 *         order to validate generic daos and services behaviours with a non
 *         resource dervied entity
 * 
 */
public class StandaloneEntityDaoTest {

    @Inject
    @Named("standaloneEntityDao")
    public void setDao(StandaloneEntityDao dao) {
//        this.dao = dao;
    }

//    @Override
//    public void testUpdate() throws Exception {
//        StandaloneEntity entity = new StandaloneEntity();
//        entity.setName("Name");
//        dao.saveAndFlush(entity);
//        
//        entity.setName("New name");
//        dao.saveAndFlush(entity);
//
//        StandaloneEntity updatedEntity = dao.readByPrimaryKey(entity.getId());
//        assertEquals("Entity name should have been modified", "New name", updatedEntity.getName());
//    }
}
