package org.resthub.identity.dao;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.AbstractResourceDaoTest;
import org.resthub.identity.model.User;

/**
 * 
 * @author Loïc Frering <loic.frering@atosorigin.com>
 */
public class UserDaoTest extends AbstractResourceDaoTest<User, UserDao> {

	@Inject
	@Named("userDao")
	@Override
	public void setResourceDao(UserDao resourceDao) {
		super.setResourceDao(resourceDao);
	}

	@Test
	@Override
	public void testFindAll() throws Exception {
		List<User> resourceList = resourceDao.readAll();
		System.out.println( "Taille de la liste : " + resourceList.size() );
		assertTrue("No resources found!", resourceList.size() == 1);
	}

	@Test
	@Override
	public void testUpdate() throws Exception {
		User u1 = resourceDao.readByPrimaryKey(this.getRessourceId());
		u1.setEmail("test@plop.fr");
		resourceDao.save(u1);
		
		User u2 = resourceDao.readByPrimaryKey(this.getRessourceId());
		assertEquals("User not updated!", u2.getEmail(), "test@plop.fr");
	}
}
