package org.resthub.identity.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.repository.AbstractRepositoryTest;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;

/**
 * 
 * @author Guillaume Zurbach
 */
public class UserRepositoryTest extends AbstractRepositoryTest<User, Long, UserRepository> {

	private static final String GROUP_NAME = "TestGroup";

	private static final String USER_LOGIN = "TestUserLogin";
	private static final String USER_FIRST_NAME = "TestUserFirstName";
	private static final String USER_LAST_NAME = "TestUserLastName";
	private static final String USER_PASSWORD = "TestUserPassword";

	private static final String USER_PERMISSION_1 = "ADMIN";
	private static final String USER_PERMISSION_2 = "USER";

	@Inject
	@Named("groupRepository")
	protected GroupRepository groupRepository;

	@Inject
	@Named("userRepository")
	@Override
	public void setRepository(UserRepository resourceRepository) {
		super.setRepository(resourceRepository);
	}

	@Override
	protected User createTestEntity() {
		return createTestUser();
	}

	private Group createTestGroup() {
		Group group = new Group();
		group.setName(GROUP_NAME + Math.round(Math.random() * 100));
		return group;
	}

	private User createTestUser() {
		User user = new User();
		user.setLogin(USER_LOGIN + Math.round(Math.random() * 100));
		user.setFirstName(USER_FIRST_NAME);
		user.setLastName(USER_LAST_NAME);
		user.setPassword(USER_PASSWORD);
		user.setEmail(USER_LOGIN + Math.round(Math.random() * 100) + "@test.fr");

		user.getPermissions().addAll(new ArrayList<String>(Arrays.asList(USER_PERMISSION_1, USER_PERMISSION_2)));
		return user;
	}

	@Override
	public void testUpdate() {
		User user1 = repository.findOne(this.id);
		user1.setEmail("test@plop.fr");
		repository.save(user1);

		User user2 = repository.findOne(this.id);
		assertEquals("User not updated!", user2.getEmail(), "test@plop.fr");
	}

	@Test
	public void testGetAndPermissions() {
		User user = createTestUser();
		user = repository.save(user);

		// when we search him by his login and password
		List<User> users = repository.findByLogin(user.getLogin());
		assertNotNull(users);
		assertNotNull(users.get(0));
		assertEquals(user.getLogin(), users.get(0).getLogin());
		assertNotNull(users.get(0).getPermissions());
		assertFalse(users.get(0).getPermissions().isEmpty());
		assertEquals(user.getPermissions().get(0), users.get(0).getPermissions().get(0));
		assertEquals(user.getPermissions().get(1), users.get(0).getPermissions().get(1));
	}

	@Test
	public void testGetUsersFromGroup() {
		Group group = createTestGroup();
		group = groupRepository.save(group);

		User user = createTestUser();
		user.getGroups().add(group);
		repository.save(user);

		List<User> users = this.repository.getUsersFromGroup(group.getName());
		assertNotNull("Users should not be null", users);
		assertFalse("Users should not empty", users.isEmpty());
	}
	
	@Override
	public Long getIdFromEntity(User user) {
		return user.getId();
	}
	
}
