package org.resthub.core.test.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.test.AbstractTransactionalTest;
import org.resthub.core.util.ClassUtils;
import org.resthub.core.util.MetamodelUtils;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository test base class in order to test your Repository extending
 * Pa<T, ID> This can be usefull to do a quick check that everything
 * is fine for basic CRUD functionnalities (+ Pagination)
 * 
 * Best practices are to separate these kind of automatic tests from your custom
 * tests, that may use RESThub DbUnit integration, and that should implement the
 * tests you will write.
 * 
 * @param <T>
 *            The model class managed by the repository
 * @param <ID>
 *            The ID class of the model class managed by the repository
 * @param <D>
 *            Your repository class
 */
public abstract class AbstractRepositoryTest<T, ID extends Serializable, D extends PagingAndSortingRepository<T, ID>> extends
		AbstractTransactionalTest {

	protected static final Logger LOGGER = Logger.getLogger(AbstractRepositoryTest.class);

	/**
	 * The tested Repository
	 */
	protected D repository;

	/**
	 * Id of the tested POJO
	 */
	protected ID id;

	@PersistenceContext
	private EntityManager em;

	/**
	 * Injection of Repository
	 * @param repository the repository to set
	 */
	public void setRepository(D repository) {
		this.repository = repository;
	}

	/**
	 * Automatically retrieve ID from entity instance
	 * 
	 * @param obj
	 *            The object from whom we need primary key
	 * @return The corresponding primary key.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ID getIdFromEntity(T obj) {
		MetamodelUtils utils = new MetamodelUtils<T, ID>((Class<T>) ClassUtils.getGenericTypeFromBean(this),
				em.getMetamodel());
		return (ID) utils.getIdFromEntity(obj);
	}

	@SuppressWarnings("unchecked")
	protected T createTestEntity() {
		T entity;
		try {
			entity = (T) ClassUtils.getGenericTypeFromBean(repository).newInstance();
		} catch (Exception e) {
			LOGGER.error("Error when creating the test entity : " + e);
			return null;
		}
		return entity;
	}

	@Before
	public void setUp() {
		T resource = this.createTestEntity();
		resource = repository.save(resource);
		this.id = getIdFromEntity(resource);
	}

	@After
	public void tearDown() {
		// Don't use deleteAll because it does not acheive cascade delete
		for (T resource : repository.findAll()) {
			repository.delete(resource);
		}
	}

	@Test
	public abstract void testUpdate();

	@Test
	public void testSave() {
		T resource = this.createTestEntity();
		resource = repository.save(resource);

		T foundResource = repository.findOne(getIdFromEntity(resource));
		assertNotNull("Resource not found!", foundResource);
	}

	@Test
	public void testDelete() {
		T resource = repository.findOne(this.id);
		repository.delete(resource);

		T foundResource = repository.findOne(this.id);
		assertNull("Resource not deleted!", foundResource);
	}

	@Test
	public void testDeleteById() {
		repository.delete(this.id);

		T foundResource = repository.findOne(this.id);
		assertNull("Resource not deleted!", foundResource);
	}

	@Test
	public void testFindAll() {
		List<T> resourceList = (List<T>) repository.findAll();
		assertTrue("No resources found!", resourceList.size() >= 1);
	}

	@Test
	public void testCount() {
		Long nb = repository.count();
		assertTrue("No resources found!", nb >= 1);
	}

	@Test
	public void testFindOne() {
		T foundResource = repository.findOne(this.id);
		assertNotNull("Resource not found!", foundResource);
		assertEquals("Resource does not contain the correct Id!", this.id, getIdFromEntity(foundResource));
	}
}
