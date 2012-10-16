package org.resthub.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * Base class for your non transactional Spring aware unit tests
 * ContextConfiguration is preconfigured to scan your applicationContext.xml
 * files from classpath
 */

@ContextConfiguration(locations = { "classpath*:resthubContext.xml", "classpath*:applicationContext.xml" })
public abstract class AbstractTest extends AbstractTestNGSpringContextTests {

}
