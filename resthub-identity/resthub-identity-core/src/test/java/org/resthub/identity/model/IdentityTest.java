package org.resthub.identity.model;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test class to test behavior of the identity model behavior This is
 * essentially to test permissions management
 * */
public class IdentityTest {

	private class PermissionsOwner extends AbstractPermissionsOwner {
	}

	@Test
	public void testIdentityCreation() {
		// Given a new Identity
		AbstractPermissionsOwner i = new PermissionsOwner();
		// the object should exist and the permissions list being null
		assertNotNull("The new Identity is null", i);
	}

	@Test
	public void testPermissionSettlement() {
		// Given a new Identity
		AbstractPermissionsOwner i = new PermissionsOwner();
		String p1 = "Permission1";

		// Once we set a permissions list
		i.getPermissions().add(p1);

		// The permission list defined should be the one given
		assertEquals("the permissions list is not properly setted", p1, i
				.getPermissions().get(0));
	}

	@Test
	public void testRoleSettlement() {
		AbstractPermissionsOwner owner = new PermissionsOwner();

		assertNotNull("Role list shouldn't be null", owner.getRoles());
		assertEquals("Role list should be empty", 0, owner.getRoles().size());

		final String testRoleName = "testRole";
		Role testRole = new Role(testRoleName);

		assertEquals("Name of the role should be the one set in constructor",
				testRoleName, testRole.getName());
		assertNotNull("Permissions of the role shouldn't be null",
				testRole.getPermissions());
		assertEquals("Newly created role shouldn't have permissions", 0,
				testRole.getPermissions().size());

		final String testPermissionName = "testPerm";
		testRole.getPermissions().add(testPermissionName);

		assertTrue(
				"Permissions of the role should contain the newly added permission",
				testRole.getPermissions().contains(testPermissionName));

		owner.getRoles().add(testRole);
		assertTrue("Permission owner should contain the newly created role",
				owner.getRoles().contains(testRole));
	}
}
