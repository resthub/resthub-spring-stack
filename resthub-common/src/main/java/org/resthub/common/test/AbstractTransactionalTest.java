package org.resthub.common.test;

import org.junit.runner.RunWith;
import org.resthub.common.context.ResthubXmlContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for your transactional unit tests, preconfigure Spring test
 * framework It is suitable for your DAO unit tests, that need to be
 * transactional ContextConfiguration is preconfigured to scan your
 * applicationContext.xml files from classpath
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ResthubXmlContextLoader.class, locations = { "classpath*:resthubContext.xml",
        "classpath*:applicationContext.xml" })
@TransactionConfiguration(defaultRollback = false)
@Transactional(readOnly = false)
public abstract class AbstractTransactionalTest {

}
