package org.resthub.core.test.service;

import java.io.Serializable;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.service.GenericService;
import org.resthub.core.test.AbstractResthubTest;
import org.resthub.core.util.ClassUtils;

public abstract class AbstractServiceTest<T, PK extends Serializable, D extends GenericService<T, PK>> extends AbstractResthubTest {

	// -----------------------------------------------------------------------------------------------------------------
	// Attributes

	/**
	 * The tested Service
	 */
	protected D service;

	/**
	 * The tested POJO
	 */
	protected PK id;

	// -----------------------------------------------------------------------------------------------------------------
	// Property

	/**
	 * Injection of Service.
	 */
	public void setService(D service) {
		this.service = service;
	}

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
	// Protected methods

	@SuppressWarnings("unchecked")
	protected T createTestRessource() throws Exception {
		return (T) ClassUtils.getGenericTypeFromBean(this.service).newInstance();
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Setup/finalize

	@Before
	public void setUp() throws Exception {
		T resource = service.create(this.createTestRessource());
		this.id = getIdFromObject(resource);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Tests methods

	@Test
	public void testCreate() throws Exception {
		T resource = service.create(this.createTestRessource());

		T foundResource = service.findById(getIdFromObject(resource));
		Assert.assertNotNull("Resource not created!", foundResource);
	}

	@Test
	public abstract void testUpdate() throws Exception;

	@Test
	public void testDelete() throws Exception {
		T resource = service.findById(this.id);
		service.delete(resource);

		T foundResource = service.findById(this.id);
		Assert.assertNull("Resource not deleted!", foundResource);
	}

	@Test
	public void testDeleteById() throws Exception {
		T resource = service.findById(this.id);
		service.delete(getIdFromObject(resource));

		T foundResource = service.findById(this.id);
		Assert.assertNull("Resource not deleted!", foundResource);
	}
	
	@Test
	public void testFindById() throws Exception {
		T resource = service.findById(this.id);
		Assert.assertNotNull("Resource should not be null!", resource);
		Assert.assertEquals("Resource id and resourceId should be equals!", this.id, this.getIdFromObject(resource));
	}

	@Test
	public void testFindAll() throws Exception {
		List<T> resourceList = service.findAll(null).asList();
		Assert.assertTrue("No resources found!", resourceList.size() >= 1);
	}

	@Test
	public void testCount() throws Exception {
		Long nb = service.count();
		Assert.assertTrue("No resources found!", nb >= 1);
	}
}
