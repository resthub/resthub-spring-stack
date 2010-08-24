package org.resthub.identity.dao;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.test.AbstractResourceDaoTest;
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

	/* TODO : delete this method when AbstractWebResthubTest will be transactionnal */
	@Override
	public void testFindAll() throws Exception {
		List<Group> resourceList = resourceDao.readAll();
		System.out.println( "Taille de la liste : " + resourceList.size() );
		for( Group group : resourceList ) {
			System.out.println( group.toString() );
		}
		assertTrue("No resources found!", resourceList.size() >= 1);
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
