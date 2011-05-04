package org.resthub.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.model.SpecificTableNameEntity;
import org.resthub.core.test.dao.AbstractDaoTest;

/**
 * @author jpoirier <johan.poirier@gmail.com>
 * 
 *         This Test Suite performs tests on {@link SpecificTableNameEntity}
 *         class in order to validate generic daos and services behaviours with
 *         entities named differently than their classes
 * 
 */
public class SpecificTableNameEntityDaoTest
		extends
		AbstractDaoTest<SpecificTableNameEntity, Long, GenericDao<SpecificTableNameEntity, Long>> {

	@Inject
	@Named("specificTableNameEntityDao")
	public void setDao(GenericDao<SpecificTableNameEntity, Long> dao) {
		this.dao = dao;
	}
	
	@Override
	protected SpecificTableNameEntity createTestRessource() throws Exception {
		SpecificTableNameEntity standaloneSpecificEntity = new SpecificTableNameEntity();
		standaloneSpecificEntity.setName("test");
		return standaloneSpecificEntity;
	}

	@Test
	public void testFindEquals() throws Exception {
		List<SpecificTableNameEntity> entities = this.dao
				.findEquals("name", "test");
		assertNotNull("entities should not be null", entities);
		assertFalse("entities should not empty", entities.isEmpty());
		assertTrue("entities size should be exactly 1", entities.size() == 1);
		assertEquals("entitie name should be 'test'", "test", entities.get(0)
				.getName());
	}

	@Test
	public void testFindLike() throws Exception {
		List<SpecificTableNameEntity> entities = this.dao
				.findLike("name", "t%st");
		assertNotNull("entities should not be null", entities);
		assertFalse("entities should not empty", entities.isEmpty());
		assertTrue("entities size should be exactly 1", entities.size() == 1);
		assertEquals("entitie name should be 'test'", "test", entities.get(0)
				.getName());
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testUpdate() throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
