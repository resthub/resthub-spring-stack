package org.resthub.booking.repository;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.booking.model.User;
import org.resthub.core.test.repository.AbstractRepositoryTest;

public class UserRepositoryTest extends AbstractRepositoryTest<User, Long, UserRepository> {

    private static final String CHANGED_TEST_USER_EMAIL = "user" + new Random().nextInt(10000) + "@test.com";
    private String testUsername;

    private User testUser;
    
    @Override
    @Inject
    @Named("userRepository")
    public void setRepository(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    protected User createTestEntity() {
        testUser = new User();
        this.testUsername = "user" + new Random().nextInt(10000);
        testUser.setUsername(testUsername);
        testUser.setEmail(Calendar.getInstance().getTimeInMillis() + "test@booking.user");
        testUser.setFullname("testBookingUserFullname");
        testUser.setPassword("password");
        return testUser;
    }

    @Override
    @Test
    public void testUpdate() {
    	User updatedUser = this.repository.findOne(testUser.getId());

    	updatedUser.setEmail(CHANGED_TEST_USER_EMAIL);
    	updatedUser = this.repository.save(updatedUser);
        assertEquals("user name should have been modified", CHANGED_TEST_USER_EMAIL, updatedUser.getEmail());
    }
}
