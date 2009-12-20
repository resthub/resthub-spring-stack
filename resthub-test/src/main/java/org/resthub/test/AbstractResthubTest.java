package org.resthub.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:*ResthubContext.xml", "classpath*:*ResthubContext.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class AbstractResthubTest   {

}
