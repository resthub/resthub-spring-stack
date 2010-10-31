package org.resthub.core.context.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.resthub.core.context.config.model.ConfigAbstractResource;
import org.resthub.core.context.config.model.ConfigResourceOne;
import org.resthub.core.context.config.model.ConfigResourceTwo;
import org.resthub.core.context.entities.ResthubEntitiesContext;
import org.resthub.core.model.Resource;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestEntitiesContextScanning {

	private static final String LOCATION_PREFIX = "org/resthub/core/context/config/";
	
	@Before
	public void cleanContext () {
		ResthubEntitiesContext.getInstance().clearPersistenceUnit("resthub");
	}
	
	@Test
	public void testBasePackage() {

		String[] contextFiles = { LOCATION_PREFIX+"packageOnlyContext.xml" };
		new ClassPathXmlApplicationContext(contextFiles);

		List<String> entities = ResthubEntitiesContext.getInstance().get(
				"resthub");

		assertNotNull("entities list should not be null", entities);
		assertFalse("entities should not be empty", entities.isEmpty());
		assertTrue("exactly 2 entities should have been found",
				entities.size() >= 2);

		assertFalse("entities list should not contain "
				+ Resource.class.getSimpleName(), entities
				.contains(Resource.class.getName()));

		assertTrue("entities list should contain "
				+ ConfigResourceOne.class.getSimpleName(), entities
				.contains(ConfigResourceOne.class.getName()));

		assertTrue("entities list should contain "
				+ ConfigResourceTwo.class.getSimpleName(), entities
				.contains(ConfigResourceTwo.class.getName()));
	}

	@Test
	public void testMultipleBasePackageWithResource() {

		String[] contextFiles = {
				LOCATION_PREFIX+"packageOnlyContext.xml",
				LOCATION_PREFIX+"modelContext.xml" };
		new ClassPathXmlApplicationContext(contextFiles);

		List<String> entities = ResthubEntitiesContext.getInstance().get(
				"resthub");

		assertNotNull("entities list should not be null", entities);
		assertFalse("entities should not be empty", entities.isEmpty());
		assertTrue("more than 3 entities should have been found",
				entities.size() >= 3);

		assertTrue("entities list should contain "
				+ Resource.class.getSimpleName(), entities
				.contains(Resource.class.getName()));

		assertTrue("entities list should contain "
				+ ConfigResourceOne.class.getSimpleName(), entities
				.contains(ConfigResourceOne.class.getName()));

		assertTrue("entities list should contain "
				+ ConfigResourceTwo.class.getSimpleName(), entities
				.contains(ConfigResourceTwo.class.getName()));
		
	}
	
	@Test
	public void testIncludeFilterAnnotation() {

		String[] contextFiles = { LOCATION_PREFIX+"includeFilterAnnotationContext.xml" };
		new ClassPathXmlApplicationContext(contextFiles);
		
		List<String> entities = ResthubEntitiesContext.getInstance().get(
				"resthub");
		
		assertNotNull("entities list should not be null", entities);
		assertFalse("entities should not be empty", entities.isEmpty());
		assertTrue("only one entity should have been found",
				entities.size() == 1);

		assertTrue("entities list should contain "
				+ ConfigAbstractResource.class.getSimpleName(), entities
				.contains(ConfigAbstractResource.class.getName()));
		
	}
	
	@Test
	public void testExcludeFilterAnnotation() {

		String[] contextFiles = {
				LOCATION_PREFIX+"excludeFilterAnnotationContext.xml"};
		new ClassPathXmlApplicationContext(contextFiles);

		List<String> entities = ResthubEntitiesContext.getInstance().get(
				"resthub");

		assertNotNull("entities list should not be null", entities);
		assertFalse("entities should not be empty", entities.isEmpty());
		assertTrue("At least 2 should have been found",
				entities.size() >= 2);

		assertFalse("entities list should not contain "
				+ ConfigAbstractResource.class.getSimpleName(), entities
				.contains(ConfigAbstractResource.class.getName()));

	}
}
