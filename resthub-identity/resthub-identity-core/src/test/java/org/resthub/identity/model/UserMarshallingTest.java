package org.resthub.identity.model;

import org.resthub.web.test.jaxb.AbstractResourceJaxbTest;

public class UserMarshallingTest extends AbstractResourceJaxbTest<User> {

	@Override
	protected User createTestResource() {

		User user;
		Group group1;
		Group group2;

		user = new User();
		group1 = new Group();
		group2 = new Group();

		user.setId(Long.parseLong("1"));
		user.setLogin("UserLogin");
		user.setEmail("test@check.com");
		user.setPassword("TestPassword");
		user.getPermissions().add("perm1");
		user.getPermissions().add("perm2");

		group1.setName("Group1Name");
		group2.setName("Group2Name");

		user.getGroups().add(group1);
		user.getGroups().add(group2);

		return user;
	}
}