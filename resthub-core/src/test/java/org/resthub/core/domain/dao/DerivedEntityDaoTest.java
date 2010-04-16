package org.resthub.core.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.domain.model.DerivedEntity;
import org.resthub.core.test.AbstractResourceDaoTest;

public class DerivedEntityDaoTest extends AbstractResourceDaoTest<DerivedEntity, DerivedEntityDao> {

    @Inject
    @Named("derivedEntityDao")
    @Override
	public void setResourceDao(DerivedEntityDao resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}

	@Test
	public void testFindByName() throws Exception {
		
		DerivedEntity derivedEntity = new DerivedEntity();
		derivedEntity.setName("test");
		derivedEntity = this.resourceDao.save(derivedEntity);
		
		List<DerivedEntity> entities = this.resourceDao.findByName("test");
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
