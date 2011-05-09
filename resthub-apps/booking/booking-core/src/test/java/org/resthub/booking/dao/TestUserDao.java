package org.resthub.booking.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.booking.model.User;
import org.resthub.core.test.dao.AbstractDaoTest;

public class TestUserDao extends AbstractDaoTest<User, Long, UserDao> {

	private static final String CHANGED_TEST_USER_EMAIL = "user"+new Random().nextInt(10000)+"@test.com";
	private String testUsername;

	@Override
	@Inject
	@Named("userDao")
	public void setDao(UserDao userDao) {
		this.dao = userDao;
	}
	
	@Override
	protected User createTestRessource() throws Exception {
		User user = new User ();
		this.testUsername = "user"+new Random().nextInt(10000);
		user.setUsername(testUsername);
		user.setEmail(Calendar.getInstance().getTimeInMillis()+"test@booking.user");
		user.setFullname("testBookingUserFullname");
		user.setPassword("password");
		return user;
	}

	@Override
	@Test
	public void testUpdate() throws Exception {
		
		List<User> users = this.dao.findEquals("username", this.testUsername);
		assertEquals("users list should contain an unique result", 1, users.size());
		
		User user = users.get(0);
		user.setEmail(CHANGED_TEST_USER_EMAIL);
		user = this.dao.save(user);
		assertEquals("user name should have been modified", CHANGED_TEST_USER_EMAIL, user.getEmail());
	}

}
