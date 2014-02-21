package org.resthub.jpa;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariCPTestDataSource;
import org.fest.assertions.api.Assertions;
import org.h2.jdbcx.JdbcDataSource;
import org.resthub.jpa.pool.HikariCPDataSourceTestFactory;
import org.resthub.jpa.sql.FakeDataSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.InvocationTargetException;

@ContextConfiguration(locations = {"classpath*:resthubContext.xml", "classpath:hikaricp-test-context.xml"})
@ActiveProfiles({"resthub-jpa", "resthub-pool-hikaricp"})
public class HikariCPDataSourceFactoryIntegrationTest extends AbstractTestNGSpringContextTests {

    @Inject
    @Named("hikariCPDataSourceFactory")
    private HikariCPDataSourceTestFactory hikariCPDataSourceTestFactory;

    @Inject
    @Named("dataSource")
    private HikariCPTestDataSource hikariCPTestDataSource;

    @Test
    public void testHikariDataSourceConfigResthub()
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        Assertions.assertThat(this.hikariCPTestDataSource).isNotNull();
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig()).isNotNull();

        // check Resthub default values
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getConnectionTestQuery()).isEqualTo("/* ping*/ SELECT 1");
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getDataSourceClassName()).isEqualTo("org.resthub.jpa.sql.FakeDataSource");
        Assertions.assertThat(this.hikariCPDataSourceTestFactory.getOriginalDataSourceClassName()).isEqualTo("org.h2.jdbcx.JdbcDataSource");
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getPoolName()).startsWith("ResthubDBPool");
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().isRegisterMbeans()).isEqualTo(false);

        // check concrete datasource parameters
        Assertions.assertThat(this.hikariCPTestDataSource.getDataSource()).isNotNull().isInstanceOf(FakeDataSource.class);
        JdbcDataSource ds = (JdbcDataSource) this.hikariCPTestDataSource.getDataSource();
        Assertions.assertThat(ds.getURL()).isNotNull().isEqualTo("jdbc:h2:mem:resthub");
        Assertions.assertThat(ds.getUser()).isNotNull().isEqualTo("sa");
        Assertions.assertThat(ds.getPassword()).isNotNull().isEqualTo("");

        // check that properties provided in database.properties are resolved
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getMaxLifetime()).isEqualTo(200000);
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getMinimumPoolSize()).isEqualTo(1);
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getMaximumPoolSize()).isEqualTo(10);


        // check that Hikari defaults are kept
        HikariConfig expectedDefaults = new HikariConfig();
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getAcquireIncrement()).isEqualTo(expectedDefaults.getAcquireIncrement());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getAcquireRetries()).isEqualTo(expectedDefaults.getAcquireRetries());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getAcquireRetryDelay()).isEqualTo(expectedDefaults.getAcquireRetryDelay());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getConnectionCustomizerClassName()).isEqualTo(expectedDefaults.getConnectionCustomizerClassName());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getConnectionInitSql()).isEqualTo(expectedDefaults.getConnectionInitSql());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getConnectionTimeout()).isEqualTo(expectedDefaults.getConnectionTimeout());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getDataSource()).isEqualTo(expectedDefaults.getDataSource());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getIdleTimeout()).isEqualTo(expectedDefaults.getIdleTimeout());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getLeakDetectionThreshold()).isEqualTo(expectedDefaults.getLeakDetectionThreshold());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().getTransactionIsolation()).isEqualTo(expectedDefaults.getTransactionIsolation());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().isAutoCommit()).isEqualTo(expectedDefaults.isAutoCommit());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().isJdbc4ConnectionTest()).isEqualTo(expectedDefaults.isJdbc4ConnectionTest());
        Assertions.assertThat(this.hikariCPTestDataSource.getConfig().isInitializationFailFast()).isEqualTo(expectedDefaults.isInitializationFailFast());
    }

}
