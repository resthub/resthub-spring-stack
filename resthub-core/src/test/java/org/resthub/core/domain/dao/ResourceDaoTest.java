package org.resthub.core.domain.dao;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;
import org.resthub.core.domain.model.Resource;
import org.resthub.test.AbstractResthubTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ResourceDaoTest extends AbstractResthubTest {
	
	@Inject
	private ResourceDao<Resource> resourceDaoTest;
	
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(ResourceDaoTest.class);
	
	@Test
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void testPersistResource() throws Exception {		
		
		Resource resource1 = new Resource("resource1", "resource1");
		resourceDaoTest.persistAndFlush(resource1);
		
		Resource foundResource = resourceDaoTest.findByPath(resource1.getPath());
		assertEquals(resource1.getName(), foundResource.getName());
		
	}	

}
