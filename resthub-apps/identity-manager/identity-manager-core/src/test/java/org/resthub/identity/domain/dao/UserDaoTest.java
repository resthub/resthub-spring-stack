package org.resthub.identity.domain.dao;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.AbstractResourceDaoTest;
import org.resthub.identity.dao.UserDao;
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

	@Override
	@Test
	// (expected = UnsupportedOperationException.class)
	public void testUpdate() throws Exception {

		/*
		 * User user = (User)
		 * ClassUtils.getGenericTypeFromBean(this.resourceDao) .newInstance();
		 */

		User user = resourceDao.readByPrimaryKey(this.getRessourceId());

		user.setEmail("test@plop.fr");
		user.setLogin("UserLogin");
		user.setPassword("UserPass");
		user.addPermission("Perm1");
		user.addPermission("Perm2");

		user = resourceDao.save(user);

		user = resourceDao.readByPrimaryKey(user.getId());
		assertEquals("User not updated!", user.getEmail(), "test@plop.fr");

	}
}
