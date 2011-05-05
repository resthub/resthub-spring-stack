package org.resthub.booking.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.booking.model.User;
import org.resthub.core.test.service.AbstractServiceTest;

public class TestUserService extends AbstractServiceTest<User, Long, UserService> {

	private static final String CHANGED_TEST_USER_EMAIL = "user"
			+ new Random().nextInt(10000)+"@test.com";
	
	private User user;

	@Override
	@Inject
	@Named("userService")
	public void setService(UserService userService) {
		this.service = userService;
	}

	@Override
	protected User createTestRessource() throws Exception {
		user = new User();
		user.setUsername("user" + new Random().nextInt(10000));
		user.setEmail(Calendar.getInstance().getTimeInMillis()
				+ "test@booking.user");
		user.setFullname("testBookingUserFullname");
		user.setPassword("password");
		return user;
	}

	@Override
	public Long getIdFromEntity(User user) {
		return user.getId();
	}

	@Override
	@Test
	public void testUpdate() throws Exception {

		user = this.service.findById(user.getId());
		assertNotNull("user should not be null", user);

		user.setEmail(CHANGED_TEST_USER_EMAIL);
		user = this.service.update(user);
		assertEquals("user name should have been modified",
				CHANGED_TEST_USER_EMAIL, user.getEmail());
	}

}
