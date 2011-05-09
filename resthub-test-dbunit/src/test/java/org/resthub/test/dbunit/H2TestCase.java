package org.resthub.test.dbunit;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath:h2-test-context.xml"})
public class H2TestCase extends AbstractTestToolsTestCase {

}
