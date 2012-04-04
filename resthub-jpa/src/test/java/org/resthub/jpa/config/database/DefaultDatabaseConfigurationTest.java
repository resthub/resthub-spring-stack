package org.resthub.jpa.config.database;

import java.util.Map;
import javax.inject.Inject;
import junit.framework.Assert;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.resthub.common.test.AbstractTest;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class DefaultDatabaseConfigurationTest extends AbstractTest {

    @Inject
    BasicDataSource dataSource;

    @Inject
    LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Test
    public void testDataSource() {
        Assert.assertEquals("org.h2.Driver", dataSource.getDriverClassName());
        Assert.assertEquals("jdbc:h2:mem:resthub;DB_CLOSE_DELAY=-1", dataSource.getUrl());
        Assert.assertEquals(50, dataSource.getMaxActive());
        Assert.assertEquals(1000, dataSource.getMaxWait());
        Assert.assertEquals(true, dataSource.isPoolPreparedStatements());
        Assert.assertEquals("sa", dataSource.getUsername());
        Assert.assertTrue(dataSource.getPassword().isEmpty());
    }

    @Test
    public void testEntityManagerFactory() {
        Map<String, Object> jpaProperties = entityManagerFactory.getJpaPropertyMap();
        Assert.assertEquals("org.hibernate.dialect.H2Dialect", jpaProperties.get("hibernate.dialect"));
        Assert.assertEquals("true", jpaProperties.get("hibernate.format_sql"));
        Assert.assertEquals("update", jpaProperties.get("hibernate.hbm2ddl.auto"));
        Assert.assertEquals("true", jpaProperties.get("hibernate.cache.use_second_level_cache"));
        Assert.assertEquals("net.sf.ehcache.hibernate.SingletonEhCacheProvider",
                jpaProperties.get("hibernate.cache.provider_class"));
    }
}
