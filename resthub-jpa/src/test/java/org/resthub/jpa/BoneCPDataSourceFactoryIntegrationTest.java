package org.resthub.jpa;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPTestDataSource;
import com.zaxxer.hikari.HikariConfig;
import org.fest.assertions.api.Assertions;
import org.h2.jdbcx.JdbcDataSource;
import org.resthub.jpa.pool.BoneCPDataSourceTestFactory;
import org.resthub.jpa.sql.FakeDataSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;

@ContextConfiguration(locations = {"classpath*:resthubContext.xml", "classpath:bonecp-test-context.xml"})
@ActiveProfiles({"resthub-jpa", "resthub-pool-bonecp"})
public class BoneCPDataSourceFactoryIntegrationTest extends AbstractTestNGSpringContextTests {

    @Inject
    @Named("boneCPDataSourceFactory")
    private BoneCPDataSourceTestFactory boneCPDataSourceTestFactory;

    @Inject
    @Named("dataSource")
    private BoneCPTestDataSource bonceCPTestDataSource;

    @Test
    public void testBoneCPDataSourceConfigResthub() {

        Assertions.assertThat(this.bonceCPTestDataSource).isNotNull();
        Assertions.assertThat(this.bonceCPTestDataSource.getDriverClass()).isNotNull().isEqualTo("org.h2.Driver");

        Assertions.assertThat(this.bonceCPTestDataSource.getConfig()).isNotNull();

        // check Resthub default values
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getConnectionTestStatement()).isEqualTo("/* ping*/ SELECT 1");
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getPoolName()).isEqualTo("ResthubDBPool");
        Assertions.assertThat(this.bonceCPTestDataSource.getJdbcUrl()).isNotNull().isEqualTo("jdbc:h2:mem:resthub");
        Assertions.assertThat(this.bonceCPTestDataSource.getUser()).isNotNull().isEqualTo("sa");
        Assertions.assertThat(this.bonceCPTestDataSource.getPassword()).isNotNull().isEqualTo("");
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getMinConnectionsPerPartition()).isEqualTo(2);
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getMaxConnectionsPerPartition()).isEqualTo(4);
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getPartitionCount()).isEqualTo(3);
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getIdleConnectionTestPeriodInMinutes()).isEqualTo(1);
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getStatementsCacheSize()).isEqualTo(100);

        // check that properties provided in database.properties are resolved
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getMaxConnectionAgeInSeconds()).isEqualTo(200000);


        // check that BoneCP defaults are kept
        BoneCPConfig expectedDefaults = new BoneCPConfig();
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getAcquireRetryAttempts()).isEqualTo(expectedDefaults.getAcquireRetryAttempts());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getAcquireRetryDelayInMs()).isEqualTo(expectedDefaults.getAcquireRetryDelayInMs());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getCloseConnectionWatchTimeoutInMs()).isEqualTo(expectedDefaults.getCloseConnectionWatchTimeoutInMs());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getConnectionTimeoutInMs()).isEqualTo(expectedDefaults.getConnectionTimeoutInMs());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getConnectionHookClassName()).isEqualTo(expectedDefaults.getConnectionHookClassName());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getDefaultAutoCommit()).isEqualTo(expectedDefaults.getDefaultAutoCommit());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getDefaultCatalog()).isEqualTo(expectedDefaults.getDefaultCatalog());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getDefaultReadOnly()).isEqualTo(expectedDefaults.getDefaultReadOnly());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getDefaultTransactionIsolation()).isEqualTo(expectedDefaults.getDefaultTransactionIsolation());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getIdleMaxAgeInMinutes()).isEqualTo(expectedDefaults.getIdleMaxAgeInMinutes());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getInitSQL()).isEqualTo(expectedDefaults.getInitSQL());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getPoolAvailabilityThreshold()).isEqualTo(expectedDefaults.getPoolAvailabilityThreshold());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getPoolStrategy()).isEqualTo(expectedDefaults.getPoolStrategy());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getQueryExecuteTimeLimitInMs()).isEqualTo(expectedDefaults.getQueryExecuteTimeLimitInMs());
        Assertions.assertThat(this.bonceCPTestDataSource.getConfig().getServiceOrder()).isEqualTo(expectedDefaults.getServiceOrder());
    }

}
