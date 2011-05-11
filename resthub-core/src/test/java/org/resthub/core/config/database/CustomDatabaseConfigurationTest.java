package org.resthub.core.config.database;

import javax.inject.Inject;
import junit.framework.Assert;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.resthub.core.test.AbstractResthubTest;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
@ContextConfiguration(locations={"classpath:org/resthub/core/config/database/applicationContext.xml"})
public class CustomDatabaseConfigurationTest extends AbstractResthubTest {
    
    @Inject
    BasicDataSource dataSource;
    
    @Inject
    LocalContainerEntityManagerFactoryBean entityManagerFactory;
    
    @Test
    public void testDataSource() {
        Assert.assertEquals("com.mysql.jdbc.Driver", dataSource.getDriverClassName());
        /*Assert.assertEquals("jdbc:mysql://localhost:3306/resthub-test", dataSource.getUrl());
        Assert.assertEquals(40, dataSource.getMaxActive());
        Assert.assertEquals(800, dataSource.getMaxWait());
        Assert.assertEquals(false, dataSource.isPoolPreparedStatements());
        Assert.assertEquals("resthub", dataSource.getUsername());
        Assert.assertEquals("resthub-pass", dataSource.getPassword());*/
    }
    
    @Test
    public void testEntityManagerFactory() {
        
    }
}
