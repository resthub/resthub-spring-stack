package org.resthub.jpa;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariTestDataSource;
import org.fest.assertions.api.Assertions;
import org.h2.jdbcx.JdbcDataSource;
import org.resthub.jpa.pool.HikariDataSourceTestFactory;
import org.resthub.jpa.sql.FakeDataSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.InvocationTargetException;

@ContextConfiguration(locations = {"classpath*:resthubContext.xml", "classpath:hikari-test-context.xml"})
@ActiveProfiles({"resthub-jpa"})
public class HikariDataSourceFactoryIntegrationTest extends AbstractTestNGSpringContextTests {

    @Inject
    @Named("hikariDataSourceFactory")
    private HikariDataSourceTestFactory hikariDataSourceTestFactory;

    @Inject
    @Named("dataSource")
    private HikariTestDataSource hikariTestDataSource;

    @Test
    public void testHikariDataSourceConfigResthub()
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        Assertions.assertThat(this.hikariTestDataSource).isNotNull();
        Assertions.assertThat(this.hikariTestDataSource.getConfig()).isNotNull();

        // check Resthub default values
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getConnectionTestQuery()).isEqualTo("/* ping*/ SELECT 1");
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getDataSourceClassName()).isEqualTo("org.resthub.jpa.sql.FakeDataSource");
        Assertions.assertThat(this.hikariDataSourceTestFactory.getOriginalDataSourceClassName()).isEqualTo("org.h2.jdbcx.JdbcDataSource");
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getPoolName()).startsWith("ResthubDBPool");
        Assertions.assertThat(this.hikariTestDataSource.getConfig().isRegisterMbeans()).isEqualTo(false);

        // check concrete datasource parameters
        Assertions.assertThat(this.hikariTestDataSource.getDataSource()).isNotNull().isInstanceOf(FakeDataSource.class);
        JdbcDataSource ds = (JdbcDataSource) this.hikariTestDataSource.getDataSource();
        Assertions.assertThat(ds.getURL()).isNotNull().isEqualTo("jdbc:h2:mem:resthub");
        Assertions.assertThat(ds.getUser()).isNotNull().isEqualTo("sa");
        Assertions.assertThat(ds.getPassword()).isNotNull().isEqualTo("");

        // check that properties provided in database.properties are resolved
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getMaxLifetime()).isEqualTo(200000);
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getMinimumPoolSize()).isEqualTo(1);
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getMaximumPoolSize()).isEqualTo(10);


        // check that Hikari defaults are kept
        HikariConfig expectedDefaults = new HikariConfig();
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getAcquireIncrement()).isEqualTo(expectedDefaults.getAcquireIncrement());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getAcquireRetries()).isEqualTo(expectedDefaults.getAcquireRetries());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getAcquireRetryDelay()).isEqualTo(expectedDefaults.getAcquireRetryDelay());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getConnectionCustomizerClassName()).isEqualTo(expectedDefaults.getConnectionCustomizerClassName());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getConnectionInitSql()).isEqualTo(expectedDefaults.getConnectionInitSql());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getConnectionTimeout()).isEqualTo(expectedDefaults.getConnectionTimeout());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getDataSource()).isEqualTo(expectedDefaults.getDataSource());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getIdleTimeout()).isEqualTo(expectedDefaults.getIdleTimeout());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getLeakDetectionThreshold()).isEqualTo(expectedDefaults.getLeakDetectionThreshold());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().getTransactionIsolation()).isEqualTo(expectedDefaults.getTransactionIsolation());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().isAutoCommit()).isEqualTo(expectedDefaults.isAutoCommit());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().isJdbc4ConnectionTest()).isEqualTo(expectedDefaults.isJdbc4ConnectionTest());
        Assertions.assertThat(this.hikariTestDataSource.getConfig().isInitializationFailFast()).isEqualTo(expectedDefaults.isInitializationFailFast());
    }

}
