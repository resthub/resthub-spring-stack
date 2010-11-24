package org.resthub.identity.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test class to test behavior of the identity model behavior
 * This is essentially to test permissions management
 * */
public class IdentityTest {

	@Test
	public void testIdentityCreation(){
		//Given a new Identity
		Identity i = new Identity();
		//the object should exist and the permissions list being null
		assertNotNull("The new Identity is null",i);
		assertNull("The Permission List exist",i.getPermissions());
	}
	
	@Test
	public void testPermissionSettlement(){
		//Given a new Identity
		Identity i = new Identity();
		String p1="Permission1";
		
		//Once we set a permissions list
		List<String> l = new ArrayList<String>();
		l.add(p1);
		i.setPermissions(l);
		
		//The permission list defined should be the one given 
		assertEquals("the permissions list is not properly setted",l, i.getPermissions());
		}
	
	@Test
		public void testIsThePermissionListTheSameObjectAsTheOneThatWeCanGet(){
		//Given a new Identity
		Identity i = new Identity();
		String p1="Permission1";
		String p2 = "Permission2";
		
		List<String> l = new ArrayList<String>();
		i.setPermissions(l);
		
		l.add(p2);
		assertTrue("The object between the one setted and the one given are not the same",i.getPermissions().contains(p2));
		
	}
	
	@Test
	public void testNoMultipleEgalPermission(){
		//Given a new Identity
		Identity i = new Identity();
			
		String p3 = "Permission3";
		i.addPermission(p3);
		i.addPermission(p3);
		assertTrue(i.getPermissions().contains(p3));
		i.getPermissions().remove(p3);
		assertFalse("the add Permission add a permission even if it is already set",i.getPermissions().contains(p3));
	}
	
	@Test
	public void testRemoveAllEgalPermission(){
		//Given a new Identity
		Identity i = new Identity();
	
		//if we add "manually" (this shouldn't be done) more than one time the same permission
		//and that we remove "officially" this permission
		String p4 = "Permission4";
		i.addPermission(p4);
		i.getPermissions().add(p4);
		i.removePermission(p4);
		
		//all occurrences are removed
		assertFalse("The remove method doesn't remove the permissions as many times as it has been set",i.getPermissions().contains(p4));
	}
}
