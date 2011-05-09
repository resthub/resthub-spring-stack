package org.resthub.core.model;

import junit.framework.Assert;

import org.junit.Test;

public class ResourceTest {

	private static class RedResource extends Resource {

        private static final long serialVersionUID = -8585643870699035987L;
	}

	private static class BlueResource extends Resource {

        private static final long serialVersionUID = -3822790302007053336L;
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
