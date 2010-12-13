package org.resthub.core.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for your unit tests, preconfigure Spring test framework
 * Ususally you will have to redefine @ContextConfiguration to specify your application context file in addition to resthub ones  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:resthubContext.xml", "classpath:resthubContext.xml", "classpath*:applicationContext.xml", "classpath:applicationContext.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional(readOnly = false)
public abstract class AbstractResthubTest {

}
