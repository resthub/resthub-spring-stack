package org.resthub.identity.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

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
	    public void testGetANDPermissions(){
		 //given a new user
		 User u1= new User();
		 String login="alexDao";
		 String password="alexDao-pass";
		 u1.setLogin(login);
		 u1.setPassword(password);
		 ArrayList<String> permissions = new ArrayList<String>();
		 permissions.add("ADMIN");
		 permissions.add("USER");
		 u1.getPermissions().addAll(permissions);
		 
		 resourceDao.save(u1);
		 
		 //when we search  him by his login and password
	    List<User> l  =resourceDao.findEquals("Login", login);
	    assertNotNull(l);
	    
	    u1 = l.get(0);
	    //we get the user as response
	    assertNotNull(u1);
	    assertEquals(login, u1.getLogin());
	    assertEquals(u1.getPermissions().get(0),"ADMIN");
	 }
	 
}
