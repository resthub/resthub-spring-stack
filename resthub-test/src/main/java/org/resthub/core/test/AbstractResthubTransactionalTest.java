package org.resthub.core.test;

import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for your transactional unit tests, preconfigure Spring test framework
 * It is suitable for your DAO unit tests, that need to be transactional
 * ContextConfiguration is preconfigured to scan your applicationContext.xml files from classpath  
 */
@Transactional(readOnly = false)
@TransactionConfiguration(defaultRollback = false)
@TestExecutionListeners(listeners = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		//DbUnitTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
public abstract class AbstractResthubTransactionalTest extends AbstractResthubTest {

}
