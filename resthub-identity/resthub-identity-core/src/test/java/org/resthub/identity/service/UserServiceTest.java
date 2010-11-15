package org.resthub.identity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import junit.framework.Assert;

import org.junit.Test;
import org.resthub.core.test.service.AbstractResourceServiceTest;
import org.resthub.identity.model.User;


public class UserServiceTest extends
		AbstractResourceServiceTest<User, UserService> {

	@Inject
	@Named("userService")
	@Override
	public void setResourceService(UserService resourceService) {
		super.setResourceService(resourceService);
	}

	@Test
	public void testMultiDeletion() throws Exception{
		
		User u1 = new User();
		u1.setLogin("u1");
		
		User u2 = new User();
		u2.setLogin("u2");
		
		u1=resourceService.create(u1);
		u2=resourceService.create(u2);
		
		
		resourceService.delete(u1);
		resourceService.delete(u2);
		
		
		Assert.assertNull(resourceService.findById(u1.getId()));
		Assert.assertNull(resourceService.findById(u2.getId()));
		
	}
		
	@Override
	@Test
	public void testUpdate() throws Exception {
		/* Given a new user */
		String firstName = "alexander";
		String firstNameAbr = "alex";
		String lastName = "testUpdate";

		User u = new User();
		u.setFirstName(firstName);
		u.setLastName(lastName);

		u = this.resourceService.create(u);

		// when we change is firstName
		u.setFirstName(firstNameAbr);
		u = resourceService.update(u);

		// The modification is updated
		assertEquals(u.getFirstName(), firstNameAbr);
		// throw new UnsupportedOperationException("Not supported yet.");

		this.resourceService.delete(u);
	}

	@Test
	public void shouldGetUserByAuthenticationInformationWhenOK() {
		/* Given a new user */

		String login = "alexOK";
		String password = "alex-pass";
		User u = new User();
		u.setLogin(login);
		u.setPassword(password);
		resourceService.create(u);

		/* When we search him with good login and password */
		u = resourceService.authenticateUser(login, password);

		/* Then we retrieve the good user */
		assertNotNull(u);
		assertEquals(u.getLogin(), login);
		assertEquals(u.getPassword(), password);
	
		resourceService.delete(u);
	}

	@Test
	public void shouldGetNullWhenBadPassword() {
		/* Given a new user */
		String login = "alexBadPassword";
		String password = "alex-pass";
		String badPassword = "alex-bad-pass";

		User u = new User();
		u.setLogin(login);
		u.setPassword(password);
		resourceService.create(u);
		/* When we search him providing a bad password*/
		User retrievedUser = resourceService.authenticateUser(login, badPassword);
		
		/* Then the user is not retrieved*/
		assertNull( retrievedUser);
		
		resourceService.delete(u);
	}

	@Test
	public void shouldGetNullWhenBadLogin() {
		String login = "alexBadLogin";
		String badLogin = "alex";
		String password = "alex-password";
		/* Given a new user */

		User u = new User();
		u.setLogin(login);
		u.setPassword(password);
		resourceService.create(u);

		/* When we search him providing a bad login*/
		User retrievedUser = resourceService.authenticateUser(badLogin, password);
		/* Then the user is not retrieved*/

		assertNull(retrievedUser);
		
		this.resourceService.delete(u);
	}

	@Test
	public void testPermissions() {
		/* Given a user with permissions */
		String login = "permisisonLogin";
		String password = "Password";
		List<String> permissions = new ArrayList<String>();
		permissions.add("ADMIN");
		permissions.add("USER");

		User u = new User();
		u.setLogin(login);
		u.setPassword(password);
		u.setPermissions(permissions);
		resourceService.create(u);

		/* When we retrieved him after a search*/
		u = resourceService.findByLogin(login);
		
		/* We can get the permission */
		assertTrue("Permissions not founded", u.getPermissions().contains(
				"ADMIN"));
		assertTrue("Permissions not founded", u.getPermissions().contains(
				"USER"));

		resourceService.delete(u);

	}
}
