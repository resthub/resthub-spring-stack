package org.resthub.identity.dao;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.AbstractResourceDaoTest;
import org.resthub.identity.dao.GroupDao;
import org.resthub.identity.model.Group;

public class GroupDaoTest extends AbstractResourceDaoTest<Group, GroupDao> {

	@Inject
	@Named("groupDao")
	@Override
	public void setResourceDao(GroupDao resourceDao) {
		super.setResourceDao(resourceDao);
	}

	@Override
	@Test
	/* (expected = UnsupportedOperationException.class) */
	public void testUpdate() throws Exception {

		Group group = resourceDao.readByPrimaryKey(this.getRessourceId());

		group.setName("GroupName");
		group.addPermission("Perm1");
		group.addPermission("Perm2");

		group = resourceDao.save(group);

		group = resourceDao.readByPrimaryKey(group.getId());
		assertEquals("Group not updated!", group.getName(), "GroupName");

	}
}
