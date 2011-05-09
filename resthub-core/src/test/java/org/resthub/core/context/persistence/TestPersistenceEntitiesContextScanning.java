package org.resthub.core.context.persistence;

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

public class TestPersistenceEntitiesContextScanning {

    private static final String CONFIG_PU_NAME = "config";
    private static final String DEFAULT_PU_NAME = "resthub";
    private static final String CONTEXT_BEAN_NAME = "entityListContextBean";
    private static final String LOCATION_PREFIX = "org/resthub/core/context/persistence/";

    /**
     * Test the loading of entities from a single and simple package pattern
     */
    @Test
    public void testBasePackage() {

        String[] contextFiles = { LOCATION_PREFIX + "packageOnlyContext.xml" };
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                contextFiles);

        EntityListContextBean entityListContextBean = (EntityListContextBean) context
                .getBean(CONTEXT_BEAN_NAME);

        entityListContextBean.clearPersistenceUnit(DEFAULT_PU_NAME);
        Set<String> entities = entityListContextBean.getEntities(DEFAULT_PU_NAME);

        assertNotNull("entities list should not be null", entities);
        assertFalse("entities should not be empty", entities.isEmpty());
        assertTrue("exactly 2 entities should have been found",
                entities.size() >= 2);

        assertFalse(
                "entities list should not contain "
                        + Resource.class.getSimpleName(),
                entities.contains(Resource.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceOne.class.getSimpleName(),
                entities.contains(ConfigResourceOne.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceTwo.class.getSimpleName(),
                entities.contains(ConfigResourceTwo.class.getName()));

        assertFalse("entities list should not contain "
                + ConfigResourceFour.class.getSimpleName(),
                entities.contains(ConfigResourceFour.class.getName()));

    }

    /**
     * Test the loading of entities from multiple packages declared in multiple
     * context files
     */
    @Test
    public void testMultipleBasePackageWithResource() {

        String[] contextFiles = { LOCATION_PREFIX + "packageOnlyContext.xml",
                LOCATION_PREFIX + "modelContext.xml" };

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                contextFiles);

        EntityListContextBean entityListContextBean = (EntityListContextBean) context
                .getBean(CONTEXT_BEAN_NAME);

        entityListContextBean.clearPersistenceUnit(DEFAULT_PU_NAME);
        Set<String> entities = entityListContextBean.getEntities(DEFAULT_PU_NAME);

        assertNotNull("entities list should not be null", entities);
        assertFalse("entities should not be empty", entities.isEmpty());
        assertTrue("more than 3 entities should have been found",
                entities.size() >= 3);

        assertTrue(
                "entities list should contain "
                        + Resource.class.getSimpleName(),
                entities.contains(Resource.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceOne.class.getSimpleName(),
                entities.contains(ConfigResourceOne.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceTwo.class.getSimpleName(),
                entities.contains(ConfigResourceTwo.class.getName()));

        assertFalse("entities list should not contain "
                + ConfigResourceFour.class.getSimpleName(),
                entities.contains(ConfigResourceFour.class.getName()));

    }

    /**
     * Test the loading of entities from packages declared with wilcard
     */
    @Test
    public void testPackageWithWildcards() {

        String[] contextFiles = { LOCATION_PREFIX + "wildcardContext.xml" };

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                contextFiles);

        EntityListContextBean entityListContextBean = (EntityListContextBean) context
                .getBean(CONTEXT_BEAN_NAME);

        entityListContextBean.clearPersistenceUnit(DEFAULT_PU_NAME);
        Set<String> entities = entityListContextBean.getEntities(DEFAULT_PU_NAME);

        assertNotNull("entities list should not be null", entities);
        assertFalse("entities should not be empty", entities.isEmpty());
        assertTrue("more than 3 entities should have been found",
                entities.size() >= 3);

        assertTrue(
                "entities list should contain "
                        + Resource.class.getSimpleName(),
                entities.contains(Resource.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceOne.class.getSimpleName(),
                entities.contains(ConfigResourceOne.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceTwo.class.getSimpleName(),
                entities.contains(ConfigResourceTwo.class.getName()));

        assertFalse("entities list should not contain "
                + ConfigResourceFour.class.getSimpleName(),
                entities.contains(ConfigResourceFour.class.getName()));

    }

    /**
     * Test to load the same entity multiple times (at least twice) and check
     * the unicity of its loading
     */
    @Test
    public void testMultipleBasePackageWithDoubles() {

        String[] contextFiles = { LOCATION_PREFIX + "wildcardContext.xml",
                LOCATION_PREFIX + "modelContext.xml" };

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                contextFiles);

        EntityListContextBean entityListContextBean = (EntityListContextBean) context
                .getBean(CONTEXT_BEAN_NAME);

        entityListContextBean.clearPersistenceUnit(DEFAULT_PU_NAME);
        Set<String> entities = entityListContextBean.getEntities(DEFAULT_PU_NAME);

        assertNotNull("entities list should not be null", entities);
        assertFalse("entities should not be empty", entities.isEmpty());
        assertTrue("more than 3 entities should have been found",
                entities.size() >= 3);

        assertTrue(
                "entities list should contain "
                        + Resource.class.getSimpleName(),
                entities.contains(Resource.class.getName()));

        entities.remove(Resource.class.getName());

        assertFalse(
                "entities list should not contain "
                        + Resource.class.getSimpleName(),
                entities.contains(Resource.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceOne.class.getSimpleName(),
                entities.contains(ConfigResourceOne.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceTwo.class.getSimpleName(),
                entities.contains(ConfigResourceTwo.class.getName()));

        assertFalse("entities list should not contain "
                + ConfigResourceFour.class.getSimpleName(),
                entities.contains(ConfigResourceFour.class.getName()));

    }

    /**
     * Test cutom filter config by specifying inclusion/exclusion based on
     * anntotation value
     */
    @Test
    public void testFilterAnnotation() {

        String[] contextFiles = { LOCATION_PREFIX
                + "filterAnnotationContext.xml" };

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                contextFiles);

        EntityListContextBean entityListContextBean = (EntityListContextBean) context
                .getBean(CONTEXT_BEAN_NAME);

        entityListContextBean.clearPersistenceUnit(DEFAULT_PU_NAME);
        Set<String> entities = entityListContextBean.getEntities(DEFAULT_PU_NAME);

        assertNotNull("entities list should not be null", entities);
        assertFalse("entities should not be empty", entities.isEmpty());
        assertTrue("At least 2 should have been found", entities.size() >= 2);

        assertFalse("entities list should not contain "
                + ConfigAbstractResource.class.getSimpleName(),
                entities.contains(ConfigAbstractResource.class.getName()));

        assertFalse("entities list should not contain "
                + ConfigResourceFour.class.getSimpleName(),
                entities.contains(ConfigResourceFour.class.getName()));

    }

    /**
     * Test cutom filter config by specifying inclusion/exclusion based on
     * assignation criterion
     */
    @Test
    public void testFilterAssignable() {

        String[] contextFiles = { LOCATION_PREFIX
                + "filterAssignableContext.xml" };

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                contextFiles);

        EntityListContextBean entityListContextBean = (EntityListContextBean) context
                .getBean(CONTEXT_BEAN_NAME);

        entityListContextBean.clearPersistenceUnit(DEFAULT_PU_NAME);
        Set<String> entities = entityListContextBean.getEntities(DEFAULT_PU_NAME);

        assertNotNull("entities list should not be null", entities);
        assertFalse("entities should not be empty", entities.isEmpty());
        assertTrue("more than 2 entities should have been found",
                entities.size() >= 2);

        assertFalse("entities list should not contain "
                + ConfigAbstractResource.class.getSimpleName(),
                entities.contains(ConfigAbstractResource.class.getName()));

        assertFalse(
                "entities list should not contain "
                        + Resource.class.getSimpleName(),
                entities.contains(Resource.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceOne.class.getSimpleName(),
                entities.contains(ConfigResourceOne.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceTwo.class.getSimpleName(),
                entities.contains(ConfigResourceTwo.class.getName()));

        assertFalse("entities list should not contain "
                + ConfigResourceThree.class.getSimpleName(),
                entities.contains(ConfigResourceThree.class.getName()));

        assertFalse("entities list should not contain "
                + ConfigResourceFour.class.getSimpleName(),
                entities.contains(ConfigResourceFour.class.getName()));

    }

    /**
     * Test managing configuration with multiple persistence unit names
     */
    @Test
    public void testMultiplePersistenceUnits() {

        String[] contextFiles = { LOCATION_PREFIX
                + "multiplePersistenceUnitsContext.xml" };

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                contextFiles);

        EntityListContextBean entityListContextBean = (EntityListContextBean) context
                .getBean(CONTEXT_BEAN_NAME);

        entityListContextBean.clearPersistenceUnit(DEFAULT_PU_NAME);
        entityListContextBean.clearPersistenceUnit(CONFIG_PU_NAME);

        Set<String> resthubEntities = entityListContextBean
                .getEntities(DEFAULT_PU_NAME);
        Set<String> configEntities = entityListContextBean
                .getEntities(CONFIG_PU_NAME);

        assertNotNull("resthubEntities list should not be null",
                resthubEntities);
        assertFalse("resthubEntities should not be empty",
                resthubEntities.isEmpty());
        assertTrue("more than 2 resthubEntities should have been found",
                resthubEntities.size() >= 2);

        assertTrue(
                "resthubEntities list should contain "
                        + Resource.class.getSimpleName(),
                resthubEntities.contains(Resource.class.getName()));

        assertTrue("resthubEntities list should contain "
                + ConfigResourceOne.class.getSimpleName(),
                resthubEntities.contains(ConfigResourceOne.class.getName()));

        assertFalse(
                "resthubEntities list should not contain "
                        + ConfigAbstractResource.class.getSimpleName(),
                resthubEntities.contains(ConfigAbstractResource.class.getName()));

        assertFalse("resthubEntities list should not contain "
                + ConfigResourceTwo.class.getSimpleName(),
                resthubEntities.contains(ConfigResourceTwo.class.getName()));

        assertFalse("resthubEntities list should not contain "
                + ConfigResourceThree.class.getSimpleName(),
                resthubEntities.contains(ConfigResourceThree.class.getName()));

        assertFalse("resthubEntities list should not contain "
                + ConfigResourceFour.class.getSimpleName(),
                resthubEntities.contains(ConfigResourceFour.class.getName()));

        assertNotNull("configEntities list should not be null", configEntities);
        assertFalse("configEntities should not be empty",
                configEntities.isEmpty());
        assertTrue("more than 3 configEntities should have been found",
                configEntities.size() >= 3);

        assertFalse(
                "configEntities list should not contain "
                        + Resource.class.getSimpleName(),
                configEntities.contains(Resource.class.getName()));

        assertFalse("configEntities list should not contain "
                + ConfigResourceOne.class.getSimpleName(),
                configEntities.contains(ConfigResourceOne.class.getName()));

        assertTrue("configEntities list should contain "
                + ConfigAbstractResource.class.getSimpleName(),
                configEntities.contains(ConfigAbstractResource.class.getName()));

        assertTrue("configEntities list should contain "
                + ConfigResourceTwo.class.getSimpleName(),
                configEntities.contains(ConfigResourceTwo.class.getName()));

        assertTrue("configEntities list should contain "
                + ConfigResourceThree.class.getSimpleName(),
                configEntities.contains(ConfigResourceThree.class.getName()));

        assertFalse("configEntities list should not contain "
                + ConfigResourceFour.class.getSimpleName(),
                configEntities.contains(ConfigResourceFour.class.getName()));

    }

    /**
     * Test exclude entities after having included the same entities
     */
    @Test
    public void testEntitiesExclusion() {

        String[] contextFiles = { LOCATION_PREFIX + "packageOnlyContext.xml",
                LOCATION_PREFIX + "excludeEntitiesContext.xml" };

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                contextFiles);

        EntityListContextBean entityListContextBean = (EntityListContextBean) context
                .getBean(CONTEXT_BEAN_NAME);

        entityListContextBean.clearPersistenceUnit(DEFAULT_PU_NAME);
        Set<String> entities = entityListContextBean.getEntities(DEFAULT_PU_NAME);

        assertNotNull("entities list should not be null", entities);
        assertFalse("entities should not be empty", entities.isEmpty());
        assertTrue("more than 2 entities should have been found",
                entities.size() >= 2);

        assertFalse("entities list should not contain "
                + ConfigAbstractResource.class.getSimpleName(),
                entities.contains(ConfigAbstractResource.class.getName()));

        assertFalse(
                "entities list should not contain "
                        + Resource.class.getSimpleName(),
                entities.contains(Resource.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceOne.class.getSimpleName(),
                entities.contains(ConfigResourceOne.class.getName()));

        assertTrue(
                "entities list should contain "
                        + ConfigResourceTwo.class.getSimpleName(),
                entities.contains(ConfigResourceTwo.class.getName()));

        assertFalse("entities list should not contain "
                + ConfigResourceThree.class.getSimpleName(),
                entities.contains(ConfigResourceThree.class.getName()));

    }

    /**
     * Test managing configuration with exclusion and multiple persistence unit
     * names
     */
    @Test
    public void testExcludeMultiplePersistenceUnits() {

        String[] contextFiles = { LOCATION_PREFIX
                + "excludeMultiplePersistenceUnitsContext.xml" };

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                contextFiles);

        EntityListContextBean entityListContextBean = (EntityListContextBean) context
                .getBean(CONTEXT_BEAN_NAME);

        entityListContextBean.clearPersistenceUnit(DEFAULT_PU_NAME);
        entityListContextBean.clearPersistenceUnit(CONFIG_PU_NAME);
        Set<String> resthubEntities = entityListContextBean
                .getEntities(DEFAULT_PU_NAME);
        Set<String> configEntities = entityListContextBean
                .getEntities(CONFIG_PU_NAME);

        assertNotNull("resthubEntities list should not be null",
                resthubEntities);
        assertFalse("resthubEntities should not be empty",
                resthubEntities.isEmpty());
        assertTrue("more than 2 resthubEntities should have been found",
                resthubEntities.size() >= 2);

        assertTrue(
                "resthubEntities list should contain "
                        + Resource.class.getSimpleName(),
                resthubEntities.contains(Resource.class.getName()));

        assertTrue("resthubEntities list should contain "
                + ConfigResourceOne.class.getSimpleName(),
                resthubEntities.contains(ConfigResourceOne.class.getName()));

        assertFalse(
                "resthubEntities list should not contain "
                        + ConfigAbstractResource.class.getSimpleName(),
                resthubEntities.contains(ConfigAbstractResource.class.getName()));

        assertFalse("resthubEntities list should not contain "
                + ConfigResourceTwo.class.getSimpleName(),
                resthubEntities.contains(ConfigResourceTwo.class.getName()));

        assertFalse("resthubEntities list should not contain "
                + ConfigResourceThree.class.getSimpleName(),
                resthubEntities.contains(ConfigResourceThree.class.getName()));

        assertFalse("resthubEntities list should not contain "
                + ConfigResourceFour.class.getSimpleName(),
                resthubEntities.contains(ConfigResourceFour.class.getName()));

        assertNotNull("configEntities list should not be null", configEntities);
        assertFalse("configEntities should not be empty",
                configEntities.isEmpty());
        assertTrue("more than 1 configEntities should have been found",
                configEntities.size() >= 1);

        assertFalse(
                "configEntities list should not contain "
                        + Resource.class.getSimpleName(),
                configEntities.contains(Resource.class.getName()));

        assertFalse("configEntities list should not contain "
                + ConfigResourceOne.class.getSimpleName(),
                configEntities.contains(ConfigResourceOne.class.getName()));

        assertFalse("configEntities list should not contain "
                + ConfigAbstractResource.class.getSimpleName(),
                configEntities.contains(ConfigAbstractResource.class.getName()));

        assertTrue("configEntities list should contain "
                + ConfigResourceTwo.class.getSimpleName(),
                configEntities.contains(ConfigResourceTwo.class.getName()));

        assertFalse("configEntities list should not contain "
                + ConfigResourceThree.class.getSimpleName(),
                configEntities.contains(ConfigResourceThree.class.getName()));

        assertFalse("configEntities list should not contain "
                + ConfigResourceFour.class.getSimpleName(),
                configEntities.contains(ConfigResourceFour.class.getName()));

    }

}
