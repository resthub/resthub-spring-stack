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
        Assert.assertNotNull("table name should not be null", dbUnitConfiguration.getIncludeTables().get(0));
        Assert.assertNotNull("table name should not be null", dbUnitConfiguration.getIncludeTables().get(1));
    }

    @Test
    public void shouldHaveExcludes() {
        Assert.assertNotNull(dbUnitConfiguration.getExcludeTables());
        Assert.assertEquals(1, dbUnitConfiguration.getExcludeTables().size());
        Assert.assertEquals("quartz_*", dbUnitConfiguration.getExcludeTables().get(0));
    }

}
