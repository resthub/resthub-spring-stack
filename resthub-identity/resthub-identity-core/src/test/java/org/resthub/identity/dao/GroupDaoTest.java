package org.resthub.identity.dao;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.test.dao.AbstractResourceDaoTest;
import org.resthub.identity.model.Group;

/**
 *
 * @author Guillaume Zurbach
 */
public class GroupDaoTest extends AbstractResourceDaoTest<Group, GroupDao> {

	@Inject
	@Named("groupDao")
	@Override
	public void setResourceDao(GroupDao resourceDao) {
		super.setResourceDao(resourceDao);
	}

	@Override
	public void testUpdate() throws Exception {
		Group g1 = resourceDao.readByPrimaryKey(this.getRessourceId());
		g1.setName("GroupName");
		resourceDao.save(g1);
		Group g2 = resourceDao.readByPrimaryKey(this.getRessourceId());
		assertEquals("Group not updated!", g2.getName(), "GroupName");
	}
}
