package org.resthub.identity.service;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;
import org.resthub.identity.domain.model.Group;
import org.resthub.identity.domain.model.User;
import org.resthub.test.AbstractResthubTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test cases for SiteService
 * @author Bouiaw
 */
public class IdentityServiceTest extends AbstractResthubTest {

	@Inject
	private UserService userService;

	@Inject
	private GroupService groupService;

	/**
	 * Logger 
	 */
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(IdentityServiceTest.class);
	
	@Test
	public void testCreateGroup() throws Exception {

		Group group1 = new Group("group1");
		groupService.create(group1);

		Group groupRetreived = (Group) groupService.findByName("group1");
		assertEquals(group1.getName(), groupRetreived.getName());
	}

	@Test
	public void testCreateUser() throws Exception {

		User pastis = new User("pastis");
		pastis.setPassword("toto");
		userService.create(pastis);

		User userRetreived = (User) userService.findByName("pastis");
		assertEquals(pastis.getName(), userRetreived.getName());
		assertEquals(pastis.getPassword(), userRetreived.getPassword());
	}
	
	@Test
	public void testUserPermissions() throws Exception {
		
		String perm0 = "PERM_WRITE_SITE";
		String perm1 = "PERM_READ_SITE";
		
		User pastis = new User("pastis");
		pastis.addPermission(perm0);
		pastis.addPermission(perm1);
		userService.create(pastis);
		
		User userRetreived = (User) userService.findByName("pastis");
		assertEquals(perm0, userRetreived.getPermissions().get(0));
		assertEquals(perm1, userRetreived.getPermissions().get(1));
	}
	
	@Test
	public void testGroupPermissions() throws Exception {
		
		String perm0 = "PERM_WRITE_SITE";
		String perm1 = "PERM_READ_SITE";
		
		Group group1 = new Group("group1");
		group1.addPermission(perm0);
		group1.addPermission(perm1);
		groupService.create(group1);
		
		Group groupRetreived = (Group) groupService.findByName("group1");
		assertEquals(perm0, groupRetreived.getPermissions().get(0));
		assertEquals(perm1, groupRetreived.getPermissions().get(1));
	}
	
	@Test
	public void testUserAndGroupsPermissions() throws Exception {
		
		String userPerm0 = "PERM_WRITE_SITE";
		String userPerm1 = "PERM_READ_SITE";
		String groupPerm0 = "PERM_WRITE_USER";
		String groupPerm1 = "PERM_READ_USER";
		
		Group group1 = new Group("group1");
		group1.addPermission(groupPerm0);
		group1.addPermission(groupPerm1);
		groupService.create(group1);
		
		User pastis = new User("pastis");
		pastis.addPermission(userPerm0);
		pastis.addPermission(userPerm1);
		pastis.addGroup(group1);
		userService.create(pastis);
		
		User userRetreived = (User) userService.findByName("pastis");
		assertEquals(4, userRetreived.getUserAndGroupsPermissions().size());
	}
	
}
