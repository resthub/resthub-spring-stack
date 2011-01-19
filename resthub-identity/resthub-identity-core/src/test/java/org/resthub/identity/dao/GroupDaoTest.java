package org.resthub.identity.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.resthub.core.test.dao.AbstractResourceDaoTest;
import org.resthub.identity.model.Group;

/**
 *
 * @author Guillaume Zurbach
 */
public class GroupDaoTest extends AbstractResourceDaoTest<Group, PermissionsOwnerDao<Group>> {

	@Inject
	@Named("groupDao")
	@Override
	public void setResourceDao(PermissionsOwnerDao<Group> resourceDao) {
		super.setResourceDao(resourceDao);
	}
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		String groupName="GroupTestGroupName"+Math.round(Math.random()*1000);
		Group g =new Group();
		g.setName(groupName);
		g = resourceDao.save(g);
		resourceId = g.getId();
	}

	@Override
	public void testUpdate() throws Exception {
		Group g1 = resourceDao.readByPrimaryKey(this.getRessourceId());
		g1.setName("GroupName");
		resourceDao.save(g1);
		Group g2 = resourceDao.readByPrimaryKey(this.getRessourceId());
		assertEquals("Group not updated!", g2.getName(), "GroupName");
	}
	
	@Test
	public void testSave() throws Exception {
		Group g = new Group();
		String groupName="groupTestNameSave";
		g.setName(groupName);
		g = resourceDao.save(g);

		Group foundResource = resourceDao.readByPrimaryKey(g.getId());
		assertNotNull("Resource not found!", foundResource);
	}
}
