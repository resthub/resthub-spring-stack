package org.resthub.core.test.dao;

import org.resthub.core.test.AbstractResthubTest;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.resthub.core.dao.GenericResourceDao;
import org.resthub.core.model.Resource;
import org.resthub.core.util.ClassUtils;

public abstract class AbstractResourceDaoTest<T extends Resource, D extends GenericResourceDao<T>> extends AbstractResthubTest {

	protected D resourceDao;

	protected Long resourceId;

	public void setResourceDao(D resourceDao) {
		this.resourceDao = resourceDao;
	}

        public Long getRessourceId()
        {
            return this.resourceId;
        }

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		T resource = (T) ClassUtils.getGenericTypeFromBean(this.resourceDao).newInstance();
		resource = resourceDao.save(resource);
		this.resourceId = resource.getId();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSave() throws Exception {
		T resource = (T) ClassUtils.getGenericTypeFromBean(this.resourceDao).newInstance();
		resource = resourceDao.save(resource);

		T foundResource = resourceDao.readByPrimaryKey(resource.getId());
		assertNotNull("Resource not found!", foundResource);
	}

	@Test
	public abstract void testUpdate() throws Exception;

	@Test
	public void testDelete() throws Exception {
		T resource = resourceDao.readByPrimaryKey(this.resourceId);
		resourceDao.delete(resource);

		T foundResource = resourceDao.readByPrimaryKey(this.resourceId);
		assertNull("Resource not deleted!", foundResource);
	}

	@Test
	public void testDeleteById() throws Exception {
		resourceDao.delete(this.resourceId);

		T foundResource = resourceDao.readByPrimaryKey(this.resourceId);
		assertNull("Resource not deleted!", foundResource);
	}

	@Test
	public void testFindAll() throws Exception {
		List<T> resourceList = resourceDao.readAll();
		assertTrue("No resources found!", resourceList.size() == 1);
	}

	@Test
	public void testCount() throws Exception {
		Long nb = resourceDao.count();
		assertTrue("No resources found!", nb >= 1);
	}
}
