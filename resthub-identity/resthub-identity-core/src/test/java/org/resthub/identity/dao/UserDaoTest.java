package org.resthub.identity.dao;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.test.AbstractResourceDaoTest;
import org.resthub.identity.model.User;

/**
 *
 * @author Guillaume Zurbach
 */
public class UserDaoTest extends AbstractResourceDaoTest<User, UserDao> {

	@Inject
	@Named("userDao")
	@Override
	public void setResourceDao(UserDao resourceDao) {
		super.setResourceDao(resourceDao);
	}

	@Override
	public void testUpdate() throws Exception {
		User u1 = resourceDao.readByPrimaryKey(this.getRessourceId());
		u1.setEmail("test@plop.fr");
		resourceDao.save(u1);
		User u2 = resourceDao.readByPrimaryKey(this.getRessourceId());
		assertEquals("User not updated!", u2.getEmail(), "test@plop.fr");
	}
}
