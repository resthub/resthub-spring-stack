package org.resthub.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.resthub.core.model.SpecificTableNameEntity;
import org.resthub.core.test.AbstractResthubTest;

/**
 * @author jpoirier <johan.poirier@gmail.com>
 * 
 *         This Test Suite performs tests on {@link SpecificTableNameEntity}
 *         class in order to validate generic daos and services behaviours
 *         with entities named differently than their classes
 * 
 */
public class SpecificTableNameEntityDaoTest extends AbstractResthubTest {

	@Inject
	@Named("specificTableNameEntityDao")
	private GenericDao<SpecificTableNameEntity, Long> standaloneSpecificEntityDao;

	@SuppressWarnings("unused")
	private Long standaloneSpecificEntityId;

	@Before
	public void setUp() throws Exception {
		SpecificTableNameEntity standaloneSpecificEntity = new SpecificTableNameEntity();
		standaloneSpecificEntity.setName("test");
		standaloneSpecificEntity = this.standaloneSpecificEntityDao.save(standaloneSpecificEntity);
		this.standaloneSpecificEntityId = standaloneSpecificEntity.getId();
	}

    @Test
	public void testFindEquals() throws Exception {
		List<SpecificTableNameEntity> entities = this.standaloneSpecificEntityDao.findEquals("name", "test");
		assertNotNull("entities should not be null", entities);
		assertFalse("entities should not empty", entities.isEmpty());
		assertTrue("entities size should be exactly 1", entities.size() == 1);
		assertEquals("entitie name should be 'test'","test", entities.get(0).getName());
	}

    @Test
	public void testFindLike() throws Exception {
		List<SpecificTableNameEntity> entities = this.standaloneSpecificEntityDao.findLike("name", "t%st");
		assertNotNull("entities should not be null", entities);
		assertFalse("entities should not empty", entities.isEmpty());
		assertTrue("entities size should be exactly 1", entities.size() == 1);
		assertEquals("entitie name should be 'test'","test", entities.get(0).getName());
	}
}
