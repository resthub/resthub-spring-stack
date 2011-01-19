package org.resthub.core.context.jaxb;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.resthub.core.context.model.ConfigAbstractResource;
import org.resthub.core.context.model.ConfigResourceFour;
import org.resthub.core.context.model.ConfigResourceOne;
import org.resthub.core.context.model.ConfigResourceThree;
import org.resthub.core.context.model.ConfigResourceTwo;
import org.resthub.core.model.Resource;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestJAXBElementsContextScanning {

		private static final String LOCATION_PREFIX = "org/resthub/core/context/jaxb/";

		/**
		 * Test the loading of xmlElements from a single and simple package pattern
		 */
		@Test
		public void testBasePackage() {

			String[] contextFiles = { LOCATION_PREFIX + "packageOnlyContext.xml" };
			
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextFiles);
			JAXBElementListContextBean elementListContextBean = (JAXBElementListContextBean)context.getBean("JAXBElementListContext");		
			
			elementListContextBean.clear();
			Set<String> xmlElements = elementListContextBean.getXmlElements();

			assertNotNull("xmlElements list should not be null", xmlElements);
			assertFalse("xmlElements should not be empty", xmlElements.isEmpty());
			assertTrue("at least 3 xmlElements should have been found",
					xmlElements.size() >= 3);

			assertFalse("xmlElements list should not contain "
					+ Resource.class.getSimpleName(), xmlElements
					.contains(Resource.class.getName()));

			assertTrue("xmlElements list should contain "
					+ ConfigResourceOne.class.getSimpleName(), xmlElements
					.contains(ConfigResourceOne.class.getName()));
			
			assertTrue("xmlElements list should contain "
					+ ConfigResourceThree.class.getSimpleName(), xmlElements
					.contains(ConfigResourceThree.class.getName()));
			
			assertTrue("xmlElements list should contain "
					+ ConfigAbstractResource.class.getSimpleName(), xmlElements
					.contains(ConfigAbstractResource.class.getName()));

			assertFalse("xmlElements list should not contain "
					+ ConfigResourceTwo.class.getSimpleName(), xmlElements
					.contains(ConfigResourceTwo.class.getName()));
			
			assertFalse("xmlElements list should not contain "
					+ ConfigResourceFour.class.getSimpleName(), xmlElements
					.contains(ConfigResourceFour.class.getName()));
		}

		/**
		 * Test the loading of xmlElements from multiple packages declared in multiple
		 * context files
		 */
		@Test
		public void testMultipleBasePackageWithResource() {

			String[] contextFiles = { LOCATION_PREFIX + "packageOnlyContext.xml",
					LOCATION_PREFIX + "modelContext.xml" };

			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextFiles);
			JAXBElementListContextBean elementListContextBean = (JAXBElementListContextBean)context.getBean("JAXBElementListContext");		
			
			elementListContextBean.clear();
			Set<String> xmlElements = elementListContextBean.getXmlElements();

			assertNotNull("xmlElements list should not be null", xmlElements);
			assertFalse("xmlElements should not be empty", xmlElements.isEmpty());
			assertTrue("at least 3 xmlElements should have been found",
					xmlElements.size() >= 3);

			assertTrue("xmlElements list should contain "
					+ Resource.class.getSimpleName(), xmlElements
					.contains(Resource.class.getName()));

			assertTrue("xmlElements list should contain "
					+ ConfigResourceOne.class.getSimpleName(), xmlElements
					.contains(ConfigResourceOne.class.getName()));
			
			assertTrue("xmlElements list should contain "
					+ ConfigResourceThree.class.getSimpleName(), xmlElements
					.contains(ConfigResourceThree.class.getName()));
			
			assertTrue("xmlElements list should contain "
					+ ConfigAbstractResource.class.getSimpleName(), xmlElements
					.contains(ConfigAbstractResource.class.getName()));

			assertFalse("xmlElements list should not contain "
					+ ConfigResourceTwo.class.getSimpleName(), xmlElements
					.contains(ConfigResourceTwo.class.getName()));
			
			assertFalse("xmlElements list should not contain "
					+ ConfigResourceFour.class.getSimpleName(), xmlElements
					.contains(ConfigResourceFour.class.getName()));

		}

		/**
		 * Test the loading of xmlElements from packages declared with wilcard
		 */
		@Test
		public void testPackageWithWildcards() {

			String[] contextFiles = { LOCATION_PREFIX + "wildcardContext.xml" };

			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextFiles);
			JAXBElementListContextBean elementListContextBean = (JAXBElementListContextBean)context.getBean("JAXBElementListContext");		
			
			elementListContextBean.clear();
			Set<String> xmlElements = elementListContextBean.getXmlElements();

			assertNotNull("xmlElements list should not be null", xmlElements);
			assertFalse("xmlElements should not be empty", xmlElements.isEmpty());
			assertTrue("at least 3 xmlElements should have been found",
					xmlElements.size() >= 3);

			assertTrue("xmlElements list should contain "
					+ Resource.class.getSimpleName(), xmlElements
					.contains(Resource.class.getName()));

			assertTrue("xmlElements list should contain "
					+ ConfigResourceOne.class.getSimpleName(), xmlElements
					.contains(ConfigResourceOne.class.getName()));
			
			assertTrue("xmlElements list should contain "
					+ ConfigResourceThree.class.getSimpleName(), xmlElements
					.contains(ConfigResourceThree.class.getName()));
			
			assertTrue("xmlElements list should contain "
					+ ConfigAbstractResource.class.getSimpleName(), xmlElements
					.contains(ConfigAbstractResource.class.getName()));

			assertFalse("xmlElements list should not contain "
					+ ConfigResourceTwo.class.getSimpleName(), xmlElements
					.contains(ConfigResourceTwo.class.getName()));
			
			assertFalse("xmlElements list should not contain "
					+ ConfigResourceFour.class.getSimpleName(), xmlElements
					.contains(ConfigResourceFour.class.getName()));

		}

		/**
		 * Test to load the same entity multiple times (at least twice) and check
		 * the unicity of its loading
		 */
		@Test
		public void testMultipleBasePackageWithDoubles() {

			String[] contextFiles = { LOCATION_PREFIX + "wildcardContext.xml",
					LOCATION_PREFIX + "modelContext.xml" };

			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextFiles);
			JAXBElementListContextBean elementListContextBean = (JAXBElementListContextBean)context.getBean("JAXBElementListContext");		
			
			elementListContextBean.clear();
			Set<String> xmlElements = elementListContextBean.getXmlElements();

			assertNotNull("xmlElements list should not be null", xmlElements);
			assertFalse("xmlElements should not be empty", xmlElements.isEmpty());
			assertTrue("at least 3 xmlElements should have been found",
					xmlElements.size() >= 3);

			assertTrue("xmlElements list should contain "
					+ Resource.class.getSimpleName(), xmlElements
					.contains(Resource.class.getName()));

			assertTrue("xmlElements list should contain "
					+ ConfigResourceOne.class.getSimpleName(), xmlElements
					.contains(ConfigResourceOne.class.getName()));
			
			assertTrue("xmlElements list should contain "
					+ ConfigResourceThree.class.getSimpleName(), xmlElements
					.contains(ConfigResourceThree.class.getName()));
			
			assertTrue("xmlElements list should contain "
					+ ConfigAbstractResource.class.getSimpleName(), xmlElements
					.contains(ConfigAbstractResource.class.getName()));

			assertFalse("xmlElements list should not contain "
					+ ConfigResourceTwo.class.getSimpleName(), xmlElements
					.contains(ConfigResourceTwo.class.getName()));
			
			assertFalse("xmlElements list should not contain "
					+ ConfigResourceFour.class.getSimpleName(), xmlElements
					.contains(ConfigResourceFour.class.getName()));

		}

		
		/**
		 * Test cutom filter config by specifying inclusion/exclusion based on
		 * assignation criterion
		 */
		@Test
		public void testFilterAssignable() {

			String[] contextFiles = { LOCATION_PREFIX
					+ "filterAssignableContext.xml" };

			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextFiles);
			JAXBElementListContextBean elementListContextBean = (JAXBElementListContextBean)context.getBean("JAXBElementListContext");		
			
			elementListContextBean.clear();
			Set<String> xmlElements = elementListContextBean.getXmlElements();

			assertNotNull("xmlElements list should not be null", xmlElements);
			assertFalse("xmlElements should not be empty", xmlElements.isEmpty());
			assertTrue("at least 1 xmlElements should have been found",
					xmlElements.size() >= 1);

			assertFalse("xmlElements list should not contain "
					+ Resource.class.getSimpleName(), xmlElements
					.contains(Resource.class.getName()));

			assertTrue("xmlElements list should contain "
					+ ConfigResourceOne.class.getSimpleName(), xmlElements
					.contains(ConfigResourceOne.class.getName()));
			
			assertFalse("xmlElements list should not contain "
					+ ConfigResourceThree.class.getSimpleName(), xmlElements
					.contains(ConfigResourceThree.class.getName()));
			
			assertFalse("xmlElements list should not contain "
					+ ConfigAbstractResource.class.getSimpleName(), xmlElements
					.contains(ConfigAbstractResource.class.getName()));

			assertFalse("xmlElements list should not contain "
					+ ConfigResourceTwo.class.getSimpleName(), xmlElements
					.contains(ConfigResourceTwo.class.getName()));
			
			assertFalse("xmlElements list should not contain "
					+ ConfigResourceFour.class.getSimpleName(), xmlElements
					.contains(ConfigResourceFour.class.getName()));
		}
		
		
		/**
		 * Test exclude xmlElements after having included the same xmlElements
		 */
		@Test
		public void testElementsExclusion() {

			String[] contextFiles = { LOCATION_PREFIX
					+ "packageOnlyContext.xml", LOCATION_PREFIX
					+ "excludeElementsContext.xml" };

			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextFiles);
			JAXBElementListContextBean elementListContextBean = (JAXBElementListContextBean)context.getBean("JAXBElementListContext");		
			
			elementListContextBean.clear();
			Set<String> xmlElements = elementListContextBean.getXmlElements();

			assertNotNull("xmlElements list should not be null", xmlElements);
			assertFalse("xmlElements should not be empty", xmlElements.isEmpty());
			assertTrue("at least 1 xmlElements should have been found",
					xmlElements.size() >= 1);

			assertFalse("xmlElements list should not contain "
					+ Resource.class.getSimpleName(), xmlElements
					.contains(Resource.class.getName()));

			assertTrue("xmlElements list should contain "
					+ ConfigResourceOne.class.getSimpleName(), xmlElements
					.contains(ConfigResourceOne.class.getName()));
			
			assertFalse("xmlElements list should not contain "
					+ ConfigResourceThree.class.getSimpleName(), xmlElements
					.contains(ConfigResourceThree.class.getName()));
			
			assertFalse("xmlElements list should not contain "
					+ ConfigAbstractResource.class.getSimpleName(), xmlElements
					.contains(ConfigAbstractResource.class.getName()));

			assertFalse("xmlElements list should not contain "
					+ ConfigResourceTwo.class.getSimpleName(), xmlElements
					.contains(ConfigResourceTwo.class.getName()));
			
			assertFalse("xmlElements list should not contain "
					+ ConfigResourceFour.class.getSimpleName(), xmlElements
					.contains(ConfigResourceFour.class.getName()));

		}
		
}
