package org.resthub.identity.model;

import org.resthub.web.test.jaxb.AbstractResourceJaxbTest;

public class GroupMarshallingTest extends AbstractResourceJaxbTest<Group> {

	@Override
	protected Group createTestResource() {

		Group group;
		User user1;
		User user2;

		group = new Group();
		user1 = new User();
		user2 = new User();

		group.setName("GroupName");
		group.addPermission("perm1");
		group.addPermission("perm2");

		user1.setLogin("User1Login");
		user2.setLogin("User2Login");

		group.addUser(user1);
		group.addUser(user2);
		user1.addGroup(group);
		
		return group;
	}
}
