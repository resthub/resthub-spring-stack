package org.resthub.test.dbunit.namespace;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath:namespace/test-namespace-includes-context.xml" })
public class H2NamespaceIncludesTestCase extends H2NamespaceTestCase {

	@Test
	public void shouldHaveIncludes() {
		Assert.assertNotNull(dbUnitConfiguration.getIncludeTables());
		Assert.assertEquals(2, dbUnitConfiguration.getIncludeTables().size());
	}
	
	@Test
	public void shouldHaveExcludes() {
		Assert.assertNotNull(dbUnitConfiguration.getExcludeTables());
		Assert.assertEquals(1, dbUnitConfiguration.getExcludeTables().size());
	}
	
}
