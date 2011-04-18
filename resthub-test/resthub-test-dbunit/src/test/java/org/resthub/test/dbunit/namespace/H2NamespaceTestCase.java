package org.resthub.test.dbunit.namespace;

import org.junit.Assert;
import org.junit.Test;
import org.resthub.test.dbunit.AbstractDBUnitTestCase;
import org.resthub.test.dbunit.config.DbUnitConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath:namespace/test-namespace-context.xml" })
public class H2NamespaceTestCase extends AbstractDBUnitTestCase {

	@Autowired
	protected DbUnitConfiguration dbUnitConfiguration;

	@Test
	public void shouldBeNotNull() {
		Assert.assertNotNull(dbUnitConfiguration);
	}

	@Test
	public void shouldHaveDataSource() {
		Assert.assertNotNull(dbUnitConfiguration.getDataSource());
	}

}
