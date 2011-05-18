package org.resthub.core.config.database;

import java.util.Map;
import javax.inject.Inject;
import junit.framework.Assert;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.resthub.core.test.AbstractTest;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
@ContextConfiguration(locations={"classpath:org/resthub/core/config/database/applicationContext.xml"})
public class CustomDatabaseConfigurationTest extends AbstractTest {
    
    @Inject
    BasicDataSource dataSource;
    
    @Inject
    LocalContainerEntityManagerFactoryBean entityManagerFactory;
    
    @Test
    public void testDataSource() {
        Assert.assertEquals("com.mysql.jdbc.Driver", dataSource.getDriverClassName());
        Assert.assertEquals("jdbc:mysql://localhost:3306/resthub-test", dataSource.getUrl());
        Assert.assertEquals(40, dataSource.getMaxActive());
        Assert.assertEquals(800, dataSource.getMaxWait());
        Assert.assertEquals(false, dataSource.isPoolPreparedStatements());
        Assert.assertEquals("resthub", dataSource.getUsername());
        Assert.assertEquals("resthub-pass", dataSource.getPassword());
    }
    
    @Test
    public void testEntityManagerFactory() {
        Map<String, Object> jpaProperties = entityManagerFactory.getJpaPropertyMap();
        Assert.assertEquals("org.hibernate.dialect.MySQLInnoDBDialect", jpaProperties.get("hibernate.dialect"));
        Assert.assertEquals("false", jpaProperties.get("hibernate.format_sql"));
        Assert.assertEquals("create", jpaProperties.get("hibernate.hbm2ddl.auto"));
        Assert.assertEquals("false", jpaProperties.get("hibernate.cache.use_second_level_cache"));
        Assert.assertEquals("org.hibernate.cache.EhCacheProvider", jpaProperties.get("hibernate.cache.provider_class"));
    }
}
