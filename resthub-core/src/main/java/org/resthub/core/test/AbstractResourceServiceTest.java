package org.resthub.core.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.service.GenericResourceService;
import org.resthub.core.util.ClassUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:resthubContext.xml", "classpath:resthubContext.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional(readOnly = false)
public abstract class AbstractResourceServiceTest<T extends Resource, D extends GenericResourceService<T>> {

	protected D resourceService;

	protected Long resourceId;

	public void setResourceService(D resourceService) {
		this.resourceService = resourceService;
	}

	protected T createTestRessource() throws Exception {
		return (T) ClassUtils.getGenericTypeFromBean(this.resourceService).newInstance();
	}

	@Before
	public void setUp() throws Exception {
		T resource = resourceService.create(this.createTestRessource());
		this.resourceId = resource.getId();
	}

	@Test
	public void testCreate() throws Exception {
		T resource = resourceService.create(this.createTestRessource());

		T foundResource = resourceService.findById(resource.getId());
		Assert.assertNotNull("Resource not created!", foundResource);
	}

	@Test
	public abstract void testUpdate() throws Exception;

	@Test
	public void testDelete() throws Exception {
		T resource = resourceService.findById(this.resourceId);
		resourceService.delete(resource);

		T foundResource = resourceService.findById(this.resourceId);
		Assert.assertNull("Resource not deleted!", foundResource);
	}

	@Test
	public void testDeleteById() throws Exception {
		T resource = resourceService.findById(this.resourceId);
		resourceService.delete(resource.getId());

		T foundResource = resourceService.findById(this.resourceId);
		Assert.assertNull("Resource not deleted!", foundResource);
	}

	@Test
	public void testFindAll() throws Exception {
		List<T> resourceList = resourceService.findAll(null, null);
		Assert.assertTrue("No resources found!", resourceList.size() >= 1);
	}

	@Test
	public void testCount() throws Exception {
		Long nb = resourceService.count();
		Assert.assertTrue("No resources found!", nb >= 1);
	}
}
