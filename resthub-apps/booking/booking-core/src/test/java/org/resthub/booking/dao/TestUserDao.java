package org.resthub.booking.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.model.User;
import org.resthub.core.test.dao.AbstractDaoTest;

public class TestUserDao extends AbstractDaoTest<User, Long, UserDao> {

	private static final String CHANGED_TEST_USER_NAME = "user"+new Random().nextInt(100);
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
		this.testUsername = "user"+new Random().nextInt(100);
		user.setUsername(testUsername);
		user.setEmail(Calendar.getInstance().getTimeInMillis()+"test@booking.user");
		user.setFullname("testBookingUserFullname");
		user.setPassword("password");
		return user;
	}
	
	@Override
	public Long getIdFromObject(User user) {
		return user.getId();
	}

	@Override
	public void testUpdate() throws Exception {
		
		List<User> users = this.dao.findEquals("username", this.testUsername);
		assertTrue("users list should contain an unique result", users.size() == 1);
		
		User user = users.get(0);
		user.setUsername(CHANGED_TEST_USER_NAME);
		user = this.dao.save(user);
		assertEquals("user name should have been modified", CHANGED_TEST_USER_NAME, user.getUsername());
	}

}
