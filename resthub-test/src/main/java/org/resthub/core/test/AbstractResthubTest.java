package org.resthub.core.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

/**
 * Base class for your non transactional Spring aware unit tests
 * ContextConfiguration is preconfigured to scan your applicationContext.xml files from classpath  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:resthubContext.xml", "classpath*:applicationContext.xml"})
@TestExecutionListeners(listeners = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class
		//DbUnitTestExecutionListener.class
		})
public abstract class AbstractResthubTest {

}
