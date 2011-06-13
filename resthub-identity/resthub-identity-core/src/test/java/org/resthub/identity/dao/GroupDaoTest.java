package org.resthub.identity.dao;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.test.dao.AbstractDaoTest;
import org.resthub.identity.model.Group;

/**
 *
 * @author Guillaume Zurbach
 */
public class GroupDaoTest extends AbstractDaoTest<Group, Long, PermissionsOwnerDao<Group>> {

	@Inject
	@Named("groupDao")
	@Override
	public void setDao(PermissionsOwnerDao<Group> dao) {
		super.setDao(dao);
	}
	
	@Override
    protected Group createTestEntity() {
		String groupName="GroupTestGroupName"+Math.round(Math.random()*100000);
		Group g =new Group();
		g.setName(groupName);
        return g;
    }

	@Override
	public void testUpdate() throws Exception {
		Group g1 = dao.readByPrimaryKey(this.id);
		g1.setName("GroupName");
		dao.save(g1);
		Group g2 = dao.readByPrimaryKey(this.id);
		assertEquals("Group not updated!", g2.getName(), "GroupName");
	}
	
}
