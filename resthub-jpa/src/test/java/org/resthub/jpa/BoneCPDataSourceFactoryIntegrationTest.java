package org.resthub.jpa;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPTestDataSource;
import org.fest.assertions.api.Assertions;
import org.resthub.jpa.pool.BoneCPDataSourceTestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;

@ContextConfiguration(locations = {"classpath:boneCPContext.xml", "classpath:bonecp-test-context.xml"})
@ActiveProfiles({"resthub-jpa", "resthub-pool-bonecp"})
public class BoneCPDataSourceFactoryIntegrationTest extends AbstractTestNGSpringContextTests {

    @Inject
    @Named("boneCPDataSourceFactory")
    private BoneCPDataSourceTestFactory boneCPDataSourceTestFactory;

    @Inject
    @Named("dataSource")
    private BoneCPTestDataSource boneCPTestDataSource;

    @Test
    public void testBoneCPDataSourceConfigResthub() {

        Assertions.assertThat(this.boneCPTestDataSource).isNotNull();
        Assertions.assertThat(this.boneCPTestDataSource.getDriverClass()).isNotNull().isEqualTo("org.h2.Driver");

        Assertions.assertThat(this.boneCPTestDataSource.getConfig()).isNotNull();

        // check Resthub default values
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getConnectionTestStatement()).isEqualTo("/* ping*/ SELECT 1");
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getPoolName()).isEqualTo("ResthubDBPool");
        Assertions.assertThat(this.boneCPTestDataSource.getJdbcUrl()).isNotNull().isEqualTo("jdbc:h2:mem:resthub;DB_CLOSE_DELAY=-1;MVCC=TRUE");
        Assertions.assertThat(this.boneCPTestDataSource.getUser()).isNotNull().isEqualTo("sa");
        Assertions.assertThat(this.boneCPTestDataSource.getPassword()).isNotNull().isEqualTo("");
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getMinConnectionsPerPartition()).isEqualTo(2);
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getMaxConnectionsPerPartition()).isEqualTo(4);
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getPartitionCount()).isEqualTo(3);
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getIdleConnectionTestPeriodInMinutes()).isEqualTo(1);
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getStatementsCacheSize()).isEqualTo(100);

        // check that properties provided in database.properties are resolved
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getMaxConnectionAgeInSeconds()).isEqualTo(200000);


        // check that BoneCP defaults are kept
        BoneCPConfig expectedDefaults = new BoneCPConfig();
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getAcquireRetryAttempts()).isEqualTo(expectedDefaults.getAcquireRetryAttempts());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getAcquireRetryDelayInMs()).isEqualTo(expectedDefaults.getAcquireRetryDelayInMs());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getCloseConnectionWatchTimeoutInMs()).isEqualTo(expectedDefaults.getCloseConnectionWatchTimeoutInMs());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getConnectionTimeoutInMs()).isEqualTo(expectedDefaults.getConnectionTimeoutInMs());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getConnectionHookClassName()).isEqualTo(expectedDefaults.getConnectionHookClassName());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getDefaultAutoCommit()).isEqualTo(expectedDefaults.getDefaultAutoCommit());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getDefaultCatalog()).isEqualTo(expectedDefaults.getDefaultCatalog());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getDefaultReadOnly()).isEqualTo(expectedDefaults.getDefaultReadOnly());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getDefaultTransactionIsolation()).isEqualTo(expectedDefaults.getDefaultTransactionIsolation());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getIdleMaxAgeInMinutes()).isEqualTo(expectedDefaults.getIdleMaxAgeInMinutes());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getInitSQL()).isEqualTo(expectedDefaults.getInitSQL());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getPoolAvailabilityThreshold()).isEqualTo(expectedDefaults.getPoolAvailabilityThreshold());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getPoolStrategy()).isEqualTo(expectedDefaults.getPoolStrategy());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getQueryExecuteTimeLimitInMs()).isEqualTo(expectedDefaults.getQueryExecuteTimeLimitInMs());
        Assertions.assertThat(this.boneCPTestDataSource.getConfig().getServiceOrder()).isEqualTo(expectedDefaults.getServiceOrder());
    }

}
