package org.resthub.core.dao;



import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.model.StandaloneEntity;
import org.resthub.core.test.dao.AbstractDaoTest;

/**
 * @author bmeurant <baptiste.meurant@gmail.com>
 * @author Loïc Frering <loic.frering@gmail.com>
 * 
 *         This Test Suite performs tests on {@link StandaloneEntity} class in
 *         order to validate generic daos and services behaviours with a non
 *         resource dervied entity
 * 
 */
public class StandaloneEntityDaoTest extends AbstractDaoTest<StandaloneEntity, Long, StandaloneEntityDao> {

    @Inject
    @Named("standaloneEntityDao")
    @Override
    public void setDao(StandaloneEntityDao dao) {
        this.dao = dao;
    }

    @Override
    public void testUpdate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
