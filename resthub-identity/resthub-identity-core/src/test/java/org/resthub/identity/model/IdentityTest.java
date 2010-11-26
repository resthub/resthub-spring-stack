package org.resthub.identity.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test class to test behavior of the identity model behavior
 * This is essentially to test permissions management
 * */
public class IdentityTest {

	private class PermissionsOwner extends AbstractPermissionsOwner{}
	
	@Test
	public void testIdentityCreation(){
		//Given a new Identity
		AbstractPermissionsOwner i = new PermissionsOwner();
		//the object should exist and the permissions list being null
		assertNotNull("The new Identity is null",i);
	}
	
	@Test
	public void testPermissionSettlement(){
		//Given a new Identity
		AbstractPermissionsOwner i = new PermissionsOwner();
		String p1="Permission1";
		
		//Once we set a permissions list
		i.getPermissions().add(p1);
		
		//The permission list defined should be the one given 
		assertEquals("the permissions list is not properly setted",p1, i.getPermissions().get(0));
		}
}
