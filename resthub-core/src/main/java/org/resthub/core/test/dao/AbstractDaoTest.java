package org.resthub.core.test.dao;

import org.resthub.core.test.AbstractResthubTest;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.resthub.core.dao.GenericDao;
import org.resthub.core.util.ClassUtils;

public abstract class AbstractDaoTest<T, PK extends Serializable, D extends GenericDao<T, PK>> extends
		AbstractResthubTest {

	// -----------------------------------------------------------------------------------------------------------------
	// Attributes

	/**
	 * The tested DAO
	 */
	protected D dao;

	/**
	 * Id of the tested POJO
	 */
	protected PK id;

	// -----------------------------------------------------------------------------------------------------------------
	// Property

	/**
	 * Injection of DAO.
	 */
	public void setDao(D dao) {
		this.dao = dao;
	} // setDao().

	// -----------------------------------------------------------------------------------------------------------------
	// Abtract methods

	/**
	 * Just implement this method to return the primary key from its object.
	 * 
	 * @param obj The object from whom we need primary key
	 * @return The corresponding primary key.
	 */
	public abstract PK getIdFromObject(T obj);

	// -----------------------------------------------------------------------------------------------------------------
	// Setup/finalize

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		T resource = (T) ClassUtils.getGenericTypeFromBean(this.dao).newInstance();
		resource = dao.save(resource);
		this.id = getIdFromObject(resource);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Tests methods

	@Test
	public abstract void testUpdate() throws Exception;

	@SuppressWarnings("unchecked")
	@Test
	public void testSave() throws Exception {
		T resource = (T) ClassUtils.getGenericTypeFromBean(this.dao).newInstance();
		resource = dao.save(resource);

		T foundResource = dao.readByPrimaryKey(getIdFromObject(resource));
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
		assertTrue("No resources found!", resourceList.size() == 1);
	}

	@Test
	public void testCount() throws Exception {
		Long nb = dao.count();
		assertTrue("No resources found!", nb >= 1);
	}
}
