package org.resthub.core.domain.dao;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;
import org.resthub.core.domain.model.Resource;
import org.resthub.test.AbstractResthubTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceDaoTest extends AbstractResthubTest {
	
	@Inject
	private ResourceDao<Resource> testResourceDao;
	
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(ResourceDaoTest.class);
	
	@Test
	public void testPersistResource() throws Exception {		
		
		Resource resource1 = new Resource ("resource1", "resource1");
		testResourceDao.persist(resource1);
		
		Resource  foundResource = testResourceDao.findByPath(resource1.getPath());
		assertEquals(resource1.getName(), foundResource.getName());
		assertEquals(resource1.getPath(), foundResource.getPath());
		
	}	

}
