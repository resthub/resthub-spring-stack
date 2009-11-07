/**
 *  This file is part of Resthub.
 *
 *  Resthub is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *   Resthub is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Resthub.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.resthub.core.service;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.resthub.core.AbstractJcrTest;
import org.resthub.core.domain.model.identity.Group;
import org.resthub.core.domain.model.identity.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test cases for SiteService
 * @author Bouiaw
 */
public class IdentityServiceTest extends AbstractJcrTest {

	@Resource
	private ResourceService userService;

	@Resource
	private ResourceService groupService;

	/**
	 * Logger 
	 */
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(IdentityServiceTest.class);
	
	@Test
	public void testCreateGroup() throws Exception {

		Group group1 = new Group("group1");
		groupService.create(group1);

		Group groupRetreived = (Group) groupService.retreive("group1");
		assertEquals(group1.getName(), groupRetreived.getName());
	}

	@Test
	public void testCreateUser() throws Exception {

		User pastis = new User("pastis");
		pastis.setPassword("toto");
		userService.create(pastis);

		User userRetreived = (User) userService.retreive("pastis");
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
		
		User userRetreived = (User) userService.retreive("pastis");
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
		
		Group groupRetreived = (Group) groupService.retreive("group1");
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
		group1 = (Group)groupService.create(group1);
		
		User pastis = new User("pastis");
		pastis.addPermission(userPerm0);
		pastis.addPermission(userPerm1);
		pastis.addGroup(group1);
		userService.create(pastis);
		
		User userRetreived = (User) userService.retreive("pastis");
		assertEquals(4, userRetreived.getUserAndGroupsPermissions().size());
	}
	
}
