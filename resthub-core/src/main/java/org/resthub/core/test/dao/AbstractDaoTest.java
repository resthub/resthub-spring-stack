package org.resthub.core.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.dao.GenericDao;
import org.resthub.core.test.AbstractResthubTransactionalTest;
import org.resthub.core.util.ClassUtils;
import org.resthub.core.util.MetamodelUtils;

public abstract class AbstractDaoTest<T, PK extends Serializable, D extends GenericDao<T, PK>>
		extends AbstractResthubTransactionalTest {

	/**
	 * The tested DAO
	 */
	protected D dao;
	/**
	 * Id of the tested POJO
	 */
	protected PK id;

	@PersistenceContext
	private EntityManager em;

	/**
	 * Injection of DAO.
	 */
	public void setDao(D dao) {
		this.dao = dao;
	}

	/**
	 * Automatically retrieve ID from entity instance.
	 * 
	 * @param obj
	 *            The object from whom we need primary key
	 * @return The corresponding primary key.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected PK getIdFromEntity(T obj) {
		MetamodelUtils utils = new MetamodelUtils<T, PK>(
				(Class<T>) ClassUtils.getGenericTypeFromBean(this.dao),
				em.getMetamodel());
		return (PK) utils.getIdFromEntity(obj);
	}

	@SuppressWarnings("unchecked")
	protected T createTestRessource() throws Exception {
		return (T) ClassUtils.getGenericTypeFromBean(this.dao).newInstance();
	}

	@Before
	public void setUp() throws Exception {
		T resource = this.createTestRessource();
		resource = dao.save(resource);
		this.id = getIdFromEntity(resource);
	}

	@After
	public void tearDown() throws Exception {
		// Don't use deleteAll because it does not acheive cascade delete
		for (T resource : dao.readAll()) {
			dao.delete(resource);
		}
	}

	@Test
	public abstract void testUpdate() throws Exception;

	@Test
	public void testSave() throws Exception {
		T resource = this.createTestRessource();
		resource = dao.save(resource);

		T foundResource = dao.readByPrimaryKey(getIdFromEntity(resource));
		assertNotNull("Resource not found!", foundResource);
	}

	@Test
	public void testDelete() throws Exception {
		T resource = dao.readByPrimaryKey(this.id);
		dao.delete(resource);

		T foundResource = dao.readByPrimaryKey(this.id);
		assertNull("Resource not deleted!", foundResource);
	}

	@Test
	public void testDeleteById() throws Exception {
		dao.delete(this.id);

		T foundResource = dao.readByPrimaryKey(this.id);
		assertNull("Resource not deleted!", foundResource);
	}

	@Test
	public void testFindAll() throws Exception {
		List<T> resourceList = dao.readAll();
		assertTrue("No resources found!", resourceList.size() >= 1);
	}

	@Test
	public void testCount() throws Exception {
		Long nb = dao.count();
		assertTrue("No resources found!", nb >= 1);
	}

	@Test
	public void testReadByPrimaryKey() throws Exception {
		T foundResource = dao.readByPrimaryKey(this.id);
		assertNotNull("Resource not found!", foundResource);
		assertEquals("Resource does not contain the correct Id!", this.id,
				getIdFromEntity(foundResource));
	}
}
