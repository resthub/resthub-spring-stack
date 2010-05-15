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
import org.resthub.core.model.StandaloneEntity;
import org.resthub.core.test.AbstractResthubTest;

/**
 * @author bmeurant <baptiste.meurant@gmail.com>
 * 
 *         This Test Suite performs tests on {@link StandaloneEntity} class in
 *         order to validate generic daos and services behaviours with a non
 *         resource dervied entity
 * 
 */
public class StandaloneEntityDaoTest extends AbstractResthubTest {

	@Inject
	@Named("standaloneEntityDao")
	private StandaloneEntityDao standaloneEntityDao;

	@SuppressWarnings("unused")
	private Long standaloneEntityId;

	@Before
	public void setUp() throws Exception {
		StandaloneEntity standaloneEntity = new StandaloneEntity();
		standaloneEntity.setName("test");
		standaloneEntity = this.standaloneEntityDao.save(standaloneEntity);
		this.standaloneEntityId = standaloneEntity.getId();
	}

	@Test
	public void testFindByName() throws Exception {
		List<StandaloneEntity> entities = this.standaloneEntityDao.findByName("test");
		assertNotNull("entities should not be null", entities);
		assertFalse("entities should not empty", entities.isEmpty());
		assertTrue("entities size should be exactly 1", entities.size() == 1);
		assertEquals("entitie name should be 'test'","test", entities.get(0).getName());
	}

        @Test
	public void testFindEquals() throws Exception {
		List<StandaloneEntity> entities = this.standaloneEntityDao.findEquals("name", "test");
		assertNotNull("entities should not be null", entities);
		assertFalse("entities should not empty", entities.isEmpty());
		assertTrue("entities size should be exactly 1", entities.size() == 1);
		assertEquals("entitie name should be 'test'","test", entities.get(0).getName());
	}

        @Test
	public void testFindLike() throws Exception {
		List<StandaloneEntity> entities = this.standaloneEntityDao.findLike("name", "t%st");
		assertNotNull("entities should not be null", entities);
		assertFalse("entities should not empty", entities.isEmpty());
		assertTrue("entities size should be exactly 1", entities.size() == 1);
		assertEquals("entitie name should be 'test'","test", entities.get(0).getName());
	}

}
