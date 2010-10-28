package org.resthub.identity.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.dao.AbstractResourceDaoTest;
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
	
	 @Test
	    public void testGetUserByAuthenticationInformation(){
		 User u1= new User();
		 String login="alexDao";
		 String password="alexDao-pass";
		 u1.setLogin(login);
		 u1.setPassword(password);
		 resourceDao.save(u1);
		 
	    u1 =resourceDao.getUserByAuthenticationInformation(login, password);
	    	assertNotNull(u1);
	    }
	   
}
