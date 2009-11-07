package org.resthub.core;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:appCtxJbpm.xml" , "classpath:appCtxGlobal.xml", "classpath:appCtxJcr.xml" })
@Transactional
public abstract class AbstractJcrTest   {

}
