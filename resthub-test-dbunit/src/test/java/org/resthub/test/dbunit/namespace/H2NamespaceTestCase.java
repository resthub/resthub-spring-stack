package org.resthub.test.dbunit.namespace;

import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.junit.Assert;
import org.junit.Test;
import org.resthub.test.dbunit.AbstractDBUnitTestCase;
import org.resthub.test.dbunit.config.DbUnitConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath:namespace/test-namespace-context.xml" })
public class H2NamespaceTestCase extends AbstractDBUnitTestCase {

	@Autowired
	protected IDatabaseTester databaseTester;
	
	@Autowired
	protected IDatabaseConnection databaseConnection;
	
	@Autowired
	protected DbUnitConfiguration dbUnitConfiguration;

	@Test
	public void configurationShouldBeNotNull() {
		Assert.assertNotNull(dbUnitConfiguration);
	}
	
	@Test
	public void connectionShouldBeNotNull() {
		Assert.assertNotNull(databaseConnection);
	}
	
	@Test
	public void testerShouldBeNotNull() {
		Assert.assertNotNull(databaseTester);
	}

	@Test
	public void shouldHaveDatabaseConnection() {
		Assert.assertNotNull(dbUnitConfiguration.getDatabaseConnection());
	}

	@Test
	public void shouldHaveDatabaseTester() {
		Assert.assertNotNull(dbUnitConfiguration.getDatabaseTester());
	}
	
}
