package org.resthub.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Base class for your transactional unit tests, preconfigure Spring test
 * framework It is suitable for your DAO unit tests, that need to be
 * transactional ContextConfiguration is preconfigured to scan your
 * applicationContext.xml files from classpath
 */
@ContextConfiguration(locations = { "classpath*:resthubContext.xml", "classpath*:applicationContext.xml" })
@TransactionConfiguration(defaultRollback = false)
public abstract class AbstractTransactionalTest extends AbstractTransactionalTestNGSpringContextTests {

}
