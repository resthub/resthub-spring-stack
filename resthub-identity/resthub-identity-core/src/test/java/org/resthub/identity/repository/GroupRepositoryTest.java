package org.resthub.identity.repository;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.test.repository.AbstractRepositoryTest;
import org.resthub.identity.model.Group;

/**
 * 
 * @author Guillaume Zurbach
 */
public class GroupRepositoryTest extends AbstractRepositoryTest<Group, Long, GroupRepository> {

	private static final String GROUP_NAME = "TestGroup";
	private static final String NEW_GROUP_NAME = "NewGroup";

	@Inject
	@Named("groupRepository")
	@Override
	public void setRepository(GroupRepository groupRepository) {
		super.setRepository(groupRepository);
	}

	@Override
	protected Group createTestEntity() {
		return createTestGroup();
	}

	private Group createTestGroup() {
		Group group = new Group();
		group.setName(GROUP_NAME + Math.round(Math.random() * 100));
		return group;
	}

	@Override
	public void testUpdate() {
		Group group1 = repository.findOne(this.id);
		group1.setName(NEW_GROUP_NAME);
		repository.save(group1);

		Group group2 = repository.findOne(this.id);
		assertEquals("Group not updated!", group2.getName(), NEW_GROUP_NAME);
	}
}
