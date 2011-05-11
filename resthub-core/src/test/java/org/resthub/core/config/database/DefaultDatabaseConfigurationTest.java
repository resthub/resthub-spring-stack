package org.resthub.core.config.database;

import java.util.Map;
import javax.inject.Inject;
import junit.framework.Assert;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.resthub.core.test.AbstractResthubTest;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public class DefaultDatabaseConfigurationTest extends AbstractResthubTest {
    
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
        Assert.assertEquals("net.sf.ehcache.hibernate.SingletonEhCacheProvider", jpaProperties.get("hibernate.cache.provider_class"));
    }
}
