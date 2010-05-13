package org.resthub.core.model;

import junit.framework.Assert;

import org.junit.Test;

public class ResourceTest {

	@SuppressWarnings("serial")
	class RedResource extends Resource {
		public RedResource() {
		}
		
	}

	@SuppressWarnings("serial")
	class BlueResource extends Resource {
		public BlueResource() {
		}
	}

	@Test
	public void testSameResourcesEqualsShouldbeEquals() {
		Assert.assertTrue(new RedResource().equals(new RedResource()));
	}

	@Test
	public void testResourcesOfDifferentTypesShoudNotBeEquals() {
		Assert.assertFalse(new RedResource().equals(new BlueResource()));
	}

	@Test
	public void testResourcesWithSameIdShouldBeEquals() {
		RedResource r1 = new RedResource();
		r1.setId(123L);
		RedResource r2 = new RedResource();
		r2.setId(123L);
		Assert.assertTrue(r1.equals(r2));
	}

	@Test
	public void testResourcesWithDiffentIdShouldNotBeEquals() {
		RedResource r1 = new RedResource();
		r1.setId(123L);
		RedResource r2 = new RedResource();
		r2.setId(1234L);
		Assert.assertFalse(r1.equals(r2));
	}


}
